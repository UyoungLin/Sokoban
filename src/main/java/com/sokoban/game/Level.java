package com.sokoban.game;

import java.awt.*;
import java.util.Iterator;
import java.util.List;

import static com.sokoban.game.GameGrid.translatePoint;

/**
 * The class Level stores information about the elements in each level.
 * @author Yuyang LIN-modified
 */
public final class Level implements Iterable<GameObject> {

    private final String name;
    private final GameGrid objectsGrid;
    private final GameGrid diamondsGrid;
    private final int index;
    private int numberOfDiamonds = 0;
    private Point keeperPosition = new Point(0, 0);

    /**
     * Get the name of this level.
     * @return {@code name} level name
     */
    public String getName() {
        return name;
    }

    /**
     * Get the index of this level.
     * @return {@code index}    level index
     */
    public int getIndex() {
        return index;
    }

    /**
     * Get the keeper position.
     * @return {@code keeperPosition}   level name
     */
    public Point getKeeperPosition() {
        return keeperPosition;
    }

    /**
     * Get the target object from the source.
     * @param source    the beginning point
     * @param delta     the unit direction
     * @return {@code objectsGrid.getTargetFromSource(source, delta)}   GameObject of the target
     */
    public GameObject getTargetObject(Point source, Point delta) {
        return objectsGrid.getTargetFromSource(source, delta);
    }

    /**
     * Get the target object with the point.
     * @param p     the object of point
     * @return {@code objectsGrid.getGameObjectAt(p)}   GameObject of the target
     */
    public GameObject getObjectAt(Point p) {
        return objectsGrid.getGameObjectAt(p);
    }

    /**
     * Get the GameGrid of all the object.
     * @return {@code objectsGrid} GameGrid of objects
     */
    public GameGrid getObjectsGrid() {
        return objectsGrid;
    }

    /**
     * Constructor to initialize the the levels.
     * @param levelName     name of level
     * @param levelIndex    index of level
     * @param raw_level     object of Level
     */
    public Level(String levelName, int levelIndex, List<String> raw_level) {
        if (StartMeUp.isDebugActive()) {
            System.out.printf("[ADDING LEVEL] LEVEL [%d]: %s\n", levelIndex, levelName);
        }

        name = levelName;
        index = levelIndex;

        int rows = raw_level.size();
        int columns = raw_level.get(0).trim().length();

        objectsGrid = new GameGrid(rows, columns);
        diamondsGrid = new GameGrid(rows, columns);

        for (int row = 0; row < raw_level.size(); row++) {

            // Loop over the string one char at a time because it should be the fastest way:
            // http://stackoverflow.com/questions/8894258/fastest-way-to-iterate-over-all-the-chars-in-a-string
            for (int col = 0; col < raw_level.get(row).length(); col++) {
                // The game object is null when the we're adding a floor or a diamond
                GameObject curTile = GameObject.fromChar(raw_level.get(row).charAt(col));

                if (curTile == GameObject.DIAMOND) {
                    numberOfDiamonds++;
                    diamondsGrid.putGameObjectAt(curTile, row, col);
                    curTile = GameObject.FLOOR;
                } else if (curTile == GameObject.KEEPER) {
                    keeperPosition = new Point(row, col);
                }

                objectsGrid.putGameObjectAt(curTile, row, col);
                curTile = null;
            }
        }
    }

    /**
     * To check if all the crates are on the proper position.
     * @return {@code true} if the level is completed, {@code false} otherwise
     */
    boolean isComplete() {
        int cratedDiamondsCount = 0;
        for (int row = 0; row < objectsGrid.ROWS; row++) {
            for (int col = 0; col < objectsGrid.COLUMNS; col++) {
                if (objectsGrid.getGameObjectAt(col, row) == GameObject.CRATE
                        && diamondsGrid.getGameObjectAt(col, row) == GameObject.DIAMOND) {
                    cratedDiamondsCount++;
                }
            }
        }

        return cratedDiamondsCount >= numberOfDiamonds;
    }

    /**
     * Move the GameObject with source and delta coordinates
     * @param object    the object to move
     * @param source    the coordinates of the source
     * @param delta     the coordinates of the delta
     */
    public void moveGameObjectBy(GameObject object, Point source, Point delta) {
        moveGameObjectTo(object, source, translatePoint(source, delta));
    }

    /**
     * Move the GameObject from source to destination.
     * @param object    the object to move
     * @param source    the coordinates of the source
     * @param destination     the coordinates of the destination
     */
    public void moveGameObjectTo(GameObject object, Point source, Point destination) {
        objectsGrid.putGameObjectAt(getObjectAt(destination), source);
        objectsGrid.putGameObjectAt(object, destination);
    }

    /**
     * Override of {@link String#toString()}.
     * @return {@code objectsGrid.toString()} all GameObjects in the level
     */
    @Override
    public String toString() {
        return objectsGrid.toString();
    }

    /**
     * Override of {@link Iterable#iterator()}.
     * @return {@code new GridIterator()} iterator of level
     */
    @Override
    public Iterator<GameObject> iterator() {
        return new LevelIterator();
    }

    /**
     * An inner class to go through all the levels.
     */
    public class LevelIterator implements Iterator<GameObject> {

        int column = 0;
        int row = 0;

        /**
         * Get the current position.
         * @return {@code new Point(column, row)}   point of current position
         */
        public Point getCurrentPosition() {
            return new Point(column, row);
        }

        /**
         * To check if it is out of the bounds of grids.
         * @return {@code true} if it is not out of bounds, {@code false} otherwise
         */
        @Override
        public boolean hasNext() {
            return !(row == objectsGrid.ROWS - 1 && column == objectsGrid.COLUMNS);
        }

        /**
         * Get the next object.
         * @return {@code retObj}   the object of next
         */
        @Override
        public GameObject next() {
            if (column >= objectsGrid.COLUMNS) {
                column = 0;
                row++;
            }

            GameObject object = objectsGrid.getGameObjectAt(column, row);
            GameObject diamond = diamondsGrid.getGameObjectAt(column, row);
            GameObject retObj = object;

            column++;

            if (diamond == GameObject.DIAMOND) {
                if (object == GameObject.CRATE) {
                    retObj = GameObject.CRATE_ON_DIAMOND;
                } else if (object == GameObject.FLOOR) {
                    retObj = diamond;
                } else {
                    retObj = object;
                }
            }

            return retObj;
        }
    }
}