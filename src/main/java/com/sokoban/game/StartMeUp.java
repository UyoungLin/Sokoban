package com.sokoban.game;

import com.sokoban.logger.GameLogger;
import com.sokoban.model.MusicModel;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.KeyCode;
import javax.sound.sampled.LineUnavailableException;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * The class StartMeUp create a gameEngine for the game setup.
 * @author Yuyang LIN-modified
 */
public class StartMeUp {

    private static StartMeUp m_instance;
    public static final String GAME_NAME = "BestSokobanEverV6";
    public static GameLogger logger;
    private static boolean m_debug = false;
    private Level currentLevel;
    private String mapSetName;
    private List<Level> levels;
    private boolean gameComplete = false;
    private boolean levelComplete = false;
    private int movesCount = 0;
    private long timeCount = 0;
    private MusicModel music;
    private short direction;
    private LinkedList<Short> keeperDirection;
    private LinkedList<String> target;
    private long timeStart;
    private long timeEnd;
    private int currentMoves;
    private final short D_UP = 0;
    private final short D_RIGHT = 1;
    private final short D_DOWN = 2;
    private final short D_LEFT = 3;
    private final int TIME_CAST = 1000;

    /**
     * Check if debug mode is active.
     * @return {@code m_debug}  a boolean indicating if debug mode is on
     */
    public static boolean isDebugActive() {
        return m_debug;
    }

    /**
     * Get the total count of moves in a game.
     * @return {@code movesCount}   total count of moves in a game
     */
    public int getMovesCount() {
        return movesCount;
    }

    /**
     * Set the total count of moves in a game.
     * @param movesCount    total count of moves in a game.
     */
    public void setMovesCount(int movesCount) {
        this.movesCount = movesCount;
    }

    /**
     * Get the name of the map set.
     * @return {@code mapSetName}  the name of the map set
     */
    public String getMapSetName() {
        return mapSetName;
    }

    /**
     * Get the moving direction of each step.
     * @return {@code direction}    direction of movings
     */
    public short getDirection() {
        return direction;
    }

    /**
     * Get the linked list with all the keeper directions recorded.
     * @return {@code keeperDirection}  all the directions of keeper
     */
    public LinkedList<Short> getKeeperDirection() {
        return keeperDirection;
    }

    /**
     * Get the linked list with all the objects in front of the keeper.
     * @return {@code target}  all the objects
     */
    public LinkedList<String> getTarget() {
        return target;
    }

    /**
     * Get the start time of each level.
     * @return {@code timeStart}    the start time
     */
    public long getTimeStart() {
        return timeStart;
    }

    /**
     * Set the start time of each level.
     * @param timeStart     the start time
     */
    public void setTimeStart(long timeStart) {
        this.timeStart = timeStart;
    }

    /**
     * Get the end time of each level.
     * @return {@code timeEnd}    the end time
     */
    public long getTimeEnd() {
        return timeEnd;
    }

    /**
     * Get the total time of all the levels.
     * @return {@code timeCount}    the total time
     */
    public long getTimeCount() {
        return timeCount;
    }

    /**
     * Get the moves in current level.
     * @return {@code currentMoves}     moves count in current level
     */
    public int getCurrentMoves() {
        return currentMoves;
    }

    /**
     * Set the moves in current level.
     * @param currentMoves  moves count in current level
     */
    public void setCurrentMoves(int currentMoves) {
        this.currentMoves = currentMoves;
    }

    /**
     * Get the size of levels.
     * @return {@code this.levels.size()}   the size of levels
     */
    public int getLevelNum() {
        return this.levels.size();
    }

    /**
     * Get current level.
     * @return {@code currentLevel}     current level
     */
    public Level getCurrentLevel() {
        return currentLevel;
    }

    /**
     * Get all the levels in the file.
     * @return {@code levels} a list of all the levels
     */
    public List<Level> getLevels() {
        return levels;
    }

    /**
     * Get music model.
     * @return {@code music}    A model of music playing
     */
    public MusicModel getMusic() {
        return music;
    }

    /**
     * Get if the game is completed.
     * @return {@code true} if the game is completed, {@code false} otherwise
     */
    public boolean isGameComplete() {
        return gameComplete;
    }

    /**
     * Get if the current level is completed.
     * @return {@code true} if the level is completed, {@code false} otherwise
     */
    public boolean isLevelComplete() {
        return levelComplete;
    }

    /**
     * Set the identifier of level completion.
     * @param levelComplete     identifier of level completion
     */
    public void setLevelComplete(boolean levelComplete) {
        this.levelComplete = levelComplete;
    }

    /**
     * Turn on or turn off the debug version.
     */
    public void toggleDebug() {
        m_debug = !m_debug;
    }

    /**
     * Constructor to initialize the game from the input stream.
     * @param input     Input stream
     * @param production    the initial state of the game
     */
    private StartMeUp(InputStream input, boolean production) {
        try {
            logger = GameLogger.getInstance();
            levels = loadGameFile(input);
            currentLevel = getNextLevel();
            keeperDirection = new LinkedList<>();
            target = new LinkedList<>();
            music = new MusicModel();

            if (production) {
                music.createPlayer();
                timeStart = System.currentTimeMillis();
            }
        } catch (IOException x) {
            System.out.println("Cannot create logger.");
        } catch (NoSuchElementException e) {
            logger.warning("Cannot load the default save file: " + e.getStackTrace());
        } catch (LineUnavailableException e) {
            logger.warning("Cannot load the music file: " + e.getStackTrace());
        }
    }

    /**
     * A public method to get the instance of the class.
     * (Singleton Pattern)
     * @param input     Input stream
     * @return {@code m_instance}   the instance of the gameEngine
     */
    public static synchronized StartMeUp getInstance(InputStream input) {
        m_instance = new StartMeUp(input, true);
        try {
            input.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return m_instance;
    }

    /**
     * Get the input from the keyboard and set moves according to input.
     * @param code  Keyboard code
     */
    public void handleKey(KeyCode code) {
        switch (code) {
            case UP:
                move(new Point(-1, 0));
                direction = D_UP;
                break;

            case RIGHT:
                move(new Point(0, 1));
                direction = D_RIGHT;
                break;

            case DOWN:
                move(new Point(1, 0));
                direction = D_DOWN;
                break;

            case LEFT:
                move(new Point(0, -1));
                direction = D_LEFT;
                break;

            default:
                // Alert if the keycode is not the four keys above
                Alert all = new Alert(AlertType.WARNING);
                all.setTitle("Warning");
                all.setHeaderText("Oops");
                all.setContentText("Invalid Keyboard Input!");
                all.showAndWait();
        }

        if (isDebugActive()) {
            System.out.println(code);
        }
    }

    /**
     * Change the position of the keeper object according to delta.
     * @param delta     The unit point indicating the direction
     */
    public void move(Point delta) {
        if (isGameComplete()) {
            return;
        }

        Point keeperPosition = currentLevel.getKeeperPosition();
        GameObject keeper = currentLevel.getObjectAt(keeperPosition);
        Point targetObjectPoint = GameGrid.translatePoint(keeperPosition, delta);
        GameObject keeperTarget = currentLevel.getObjectAt(targetObjectPoint);

        if (StartMeUp.isDebugActive()) {
            System.out.println("Current level state:");
            System.out.println(currentLevel.toString());
            System.out.println("Keeper pos: " + keeperPosition);
            System.out.println("Movement source obj: " + keeper);
            System.out.printf("Target object: %s at [%s]", keeperTarget, targetObjectPoint);
        }

        boolean keeperMoved = false;

        switch (keeperTarget) {

            case WALL:
                break;

            case CRATE:

                GameObject crateTarget = currentLevel.getTargetObject(targetObjectPoint, delta);
                if (crateTarget != GameObject.FLOOR) {
                    break;
                }

                currentLevel.moveGameObjectBy(keeperTarget, targetObjectPoint, delta);
                currentLevel.moveGameObjectBy(keeper, keeperPosition, delta);
                keeperMoved = true;
                break;

            case FLOOR:
                currentLevel.moveGameObjectBy(keeper, keeperPosition, delta);
                keeperMoved = true;
                break;

            default:
                logger.severe("The object to be moved was not a recognised GameObject.");
                throw new AssertionError("This should not have happened." +
                        " Report this problem to the developer.");
        }

        if (keeperMoved) {
            keeperPosition.translate((int) delta.getX(), (int) delta.getY());
            if ((int)delta.getX() == -1 && (int)delta.getY() == 0) {
                keeperDirection.add(D_UP);
            } else if ((int)delta.getX() == 0 && (int)delta.getY() == 1) {
                keeperDirection.add(D_RIGHT);
            } else if ((int)delta.getX() == 1 && (int)delta.getY() == 0) {
                keeperDirection.add(D_DOWN);
            }else if ((int)delta.getX() == 0 && (int)delta.getY() == -1) {
                keeperDirection.add(D_LEFT);
            }

            target.add(String.valueOf(keeperTarget));

            currentMoves++;
            movesCount++;

            if (currentLevel.isComplete()) {
                levelComplete = true;
                if (isDebugActive()) {
                    System.out.println("Level complete!");
                }

                timeEnd = System.currentTimeMillis();
                long timeInterval = (timeEnd - timeStart) / TIME_CAST;
                timeCount += timeInterval;
                keeperDirection.clear();
                target.clear();
                currentLevel = getNextLevel();
            }
        }
    }

    /**
     * Read the game file and parse it to game objects.
     * @param input     Input stream
     * @return {@code levels}   All the levels in the file
     */
    public List<Level> loadGameFile(InputStream input) {
        List<Level> levels = new ArrayList<>(5);
        int levelIndex = 0;

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(input))) {
            boolean parsedFirstLevel = false;
            List<String> rawLevel = new ArrayList<>();
            String levelName = "";

            while (true) {
                String line = reader.readLine();

                // Break the loop if EOF is reached
                if (line == null) {
                    if (rawLevel.size() != 0) {
                        Level parsedLevel = new Level(levelName, levelIndex++, rawLevel);
                        levels.add(parsedLevel);
                    }
                    break;
                }

                if (line.contains("MapSetName")) {
                    mapSetName = line.replace("MapSetName: ", "");
                    continue;
                }

                if (line.contains("LevelName")) {
                    if (parsedFirstLevel) {
                        Level parsedLevel = new Level(levelName, levelIndex++, rawLevel);
                        levels.add(parsedLevel);
                        rawLevel.clear();
                    } else {
                        parsedFirstLevel = true;
                    }

                    levelName = line.replace("LevelName: ", "");
                    continue;
                }

                line = line.trim();
                line = line.toUpperCase();
                // If the line contains at least 2 WALLS, add it to the list
                if (line.matches(".*W.*W.*")) {
                    rawLevel.add(line);
                }
            }

        } catch (IOException e) {
            logger.severe("Error trying to load the game file: " + e);
        } catch (NullPointerException e) {
            logger.severe("Cannot open the requested file: " + e);
        }

        try {
            input.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return levels;
    }

    /**
     * Get the elements of next level.
     * @return {@code levels.get(0)} if it is the first level,
     * {@code levels.get(currentLevelIndex + 1)} if there has next level,
     * {@code null} if there is no next level
     */
    public Level getNextLevel() {
        if (currentLevel == null) {
            return levels.get(0);
        }

        int currentLevelIndex = currentLevel.getIndex();
        if (currentLevelIndex < levels.size() - 1) {
            levelComplete = true;
            return levels.get(currentLevelIndex + 1);
        }
        levelComplete = true;
        gameComplete = true;
        return null;
    }

    /**
     * Get the whole game grid by level
     * @param level     the level to pass
     * @return {@code newGrid}  all of the game objects in the grid
     */
    public GameGrid getGrid(Level level) {
        int col = level.getObjectsGrid().COLUMNS;
        int row = level.getObjectsGrid().ROWS;

        GameGrid newGrid = new GameGrid(col, row);
        Level.LevelIterator newLevelIterator = (Level.LevelIterator) level.iterator();

        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                newGrid.putGameObjectAt(newLevelIterator.next(), j, i);
            }
        }
        return newGrid;
    }
}
