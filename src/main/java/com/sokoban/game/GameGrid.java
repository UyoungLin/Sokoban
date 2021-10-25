package com.sokoban.game;

import java.awt.*;
import java.util.Iterator;

/**
 * The class GameGrid creates a grid for the game objects.
 * @see java.lang.Iterable
 * @author Yuyang LIN-modified
 */
public class GameGrid implements Iterable {

    final int COLUMNS;
    final int ROWS;
    private GameObject[][] gameObjects;

    /**
     * Constructor for GameGrid to initialize the GameObjects in the game.
     * @param columns   Column index
     * @param rows      Row index
     */
    public GameGrid(int columns, int rows) {
        COLUMNS = columns;
        ROWS = rows;
        gameObjects = new GameObject[COLUMNS][ROWS];
    }

    /**
     * Change the position of sourceLocation with delta.
     * @param sourceLocation    Coordinates of source
     * @param delta     the unit coordinate
     * @return {@code translatedPoint}
     */
    public static Point translatePoint(Point sourceLocation, Point delta) {
        Point translatedPoint = new Point(sourceLocation);
        translatedPoint.translate((int) delta.getX(), (int) delta.getY());
        return translatedPoint;
    }

    /**
     * Get a 2D encapsulation of the current grid.
     * @return {@code new Dimension(COLUMNS, ROWS)}
     */
    public Dimension getDimension() {
        return new Dimension(COLUMNS, ROWS);
    }

    /**
     * Get a game object after {@code translatePoint}.
     * @param source    Coordinates of source
     * @param delta     the unit coordinate
     * @return {@code getGameObjectAt(translatePoint(source, delta))}
     */
    GameObject getTargetFromSource(Point source, Point delta) {
        return getGameObjectAt(translatePoint(source, delta));
    }

    /**
     * Get the GameObject at the requested location.
     * @param col   Column index
     * @param row   Row index
     * @return {@code gameObjects[col][row]}
     * @throws ArrayIndexOutOfBoundsException   if the index of array out of bounds
     */
    public GameObject getGameObjectAt(int col, int row) throws ArrayIndexOutOfBoundsException {
        if (isPointOutOfBounds(col, row)) {
            if (StartMeUp.isDebugActive()) {
                System.out.printf("Trying to get null GameObject from COL: %d  ROW: %d", col, row);
            }
            throw new ArrayIndexOutOfBoundsException("The point [" + col + ":" + row + "] is outside the map.");
        }

        return gameObjects[col][row];
    }

    /**
     * Overload of {@link #getGameObjectAt(int col, int row)}.
     * @param p     Point
     * @return {@code gameObjects[(int) p.getX()][(int) p.getY()]}
     */
    public GameObject getGameObjectAt(Point p) {
        if (p == null) {
            throw new IllegalArgumentException("Point cannot be null.");
        }

        return getGameObjectAt((int) p.getX(), (int) p.getY());
    }

    /**
     * Remove the game object.
     * @param position  the position of the object
     * @return {@code putGameObjectAt(null, position)}
     */
    public boolean removeGameObjectAt(Point position) {
        return putGameObjectAt(null, position);
    }

    /**
     * Change the position of a GameObject, and return if the change is already down.
     * @param gameObject    gameObject to put
     * @param x     x-axis
     * @param y     y-axis
     * @return {@code true} / {@code false}
     */
    public boolean putGameObjectAt(GameObject gameObject, int x, int y) {
        if (isPointOutOfBounds(x, y)) {
            return false;
        }

        gameObjects[x][y] = gameObject;
        return gameObjects[x][y] == gameObject;
    }

    /**
     * Overload of {@link #putGameObjectAt(GameObject, int, int)}.
     * @param gameObject    gameObject to put
     * @param p     Point
     * @return {@code true} / {@code false}
     */
    public boolean putGameObjectAt(GameObject gameObject, Point p) {
        return p != null && putGameObjectAt(gameObject, (int) p.getX(), (int) p.getY());
    }

    /**
     * Check if the point is out of bounds.
     * @param x     x-axis
     * @param y     y-axis
     * @return {@code true} / {@code false}
     */
    private boolean isPointOutOfBounds(int x, int y) {
        return (x < 0 || y < 0 || x >= COLUMNS || y >= ROWS);
    }

    /**
     * Overload of {@link #isPointOutOfBounds(int, int)}.
     * @param p     Point
     * @return {@code true} / {@code false}
     */
    private boolean isPointOutOfBounds(Point p) {
        return isPointOutOfBounds(p.x, p.y);
    }

    /**
     * Override of {@link String#toString()}.
     * @return all GameObjects in the level
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(gameObjects.length);

        for (GameObject[] gameObject : gameObjects) {
            for (GameObject aGameObject : gameObject) {
                if (aGameObject == null) {
                    aGameObject = GameObject.DEBUG_OBJECT;
                }
                sb.append(aGameObject.getCharSymbol());
            }

            sb.append('\n');
        }

        return sb.toString();
    }

    /**
     * Override of {@link Iterable#iterator()}.
     * @return {@code new GridIterator()} iterator of grid
     */
    @Override
    public Iterator<GameObject> iterator() {
        return new GridIterator();
    }

    /**
     * An inner class to go through all the objects.
     */
    public class GridIterator implements Iterator<GameObject> {
        int row = 0;
        int column = 0;

        /**
         * To check if it is out of bounds.
         * @return {@code true} if it is not out of bounds, {@code false} otherwise
         */
        @Override
        public boolean hasNext() {
            return !(row == ROWS && column == COLUMNS);
        }

        /**
         * Get the next object.
         * @return {@code getGameObjectAt(column++, row)}
         */
        @Override
        public GameObject next() {
            if (column >= COLUMNS) {
                column = 0;
                row++;
            }
            return getGameObjectAt(column++, row);
        }
    }
}