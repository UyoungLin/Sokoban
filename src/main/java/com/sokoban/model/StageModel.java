package com.sokoban.model;

import com.sokoban.game.*;
import com.sokoban.game.Dialog;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.GridPane;
import javafx.scene.effect.MotionBlur;
import javafx.scene.input.KeyEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.awt.*;
import java.io.*;
import java.net.URISyntaxException;
import java.util.Scanner;
import java.util.List;

/**
 * The class StageModel Set up all the primary issues
 * and handle with the interaction between controllers and views.
 * @author Yuyang LIN
 */
public class StageModel {

    private Stage primaryStage;
    private StartMeUp gameEngine;
    private GridPane gameGrid;
    private File saveFile;
    private String name;
    private com.sokoban.game.Dialog dialogWindow;
    public static String wallColour;
    private final int TIME_CAST = 1000;
    private final int TOP_TEN = 10;
    private final short D_UP = 0;
    private final short D_RIGHT = 1;
    private final short D_DOWN = 2;
    private final short D_LEFT = 3;

    /**
     * Constructor to initialize the game grid.
     */
    public StageModel() {
        this.gameGrid = new GridPane();
        this.dialogWindow = new com.sokoban.game.Dialog(primaryStage, gameGrid);
        name = "";
    }

    /**
     * Get the gameEngine.
     * @return {@code gameEngine}
     */
    public StartMeUp getGameEngine() {
        return gameEngine;
    }

    /**
     * Get the gameGrid.
     * @return {@code gameGrid}
     */
    public GridPane getGameGrid() {
        return gameGrid;
    }

    /**
     * Get the name of player.
     * @return {@code name}
     */
    public String getName() {
        return name;
    }

    /**
     * Set the name of player.
     * @param name  player name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Get the tools to setup the dialog window.
     * @return {@code dialogWindow}
     */
    public Dialog getDialogWindow() {
        return dialogWindow;
    }

    /**
     * To get and load the default game.
     * @param primaryStage  the stage to load the game
     * @throws IOException      IO is not available
     */
    public void loadDefaultSaveFile(Stage primaryStage) throws IOException {
        this.primaryStage = primaryStage;
        System.out.println("Hi");
        InputStream in = getClass().getClassLoader().getResourceAsStream("SampleGame.skb");
        System.out.println(in);
        initializeGame(in);
        System.out.println("Hi");
        assert in != null;
        in.close();
        setEventFilter();
        System.out.println("Hi");
    }

    /**
     * Setup the gameEngine.
     * @param input     Input stream
     * @throws IOException      IO is not available
     */
    public void initializeGame(InputStream input) throws IOException {
        gameEngine = StartMeUp.getInstance(input);
        reloadGrid();
        input.close();
    }

    /**
     * Set event filter for each keyboard event.
     */
    public void setEventFilter() {
        primaryStage.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            gameEngine.handleKey(event.getCode());
            GraphicObject.giveDirection(gameEngine.getDirection());
            reloadGrid();
        });
    }

    /**
     * Load a file from other location.
     * @throws IOException      IO is not available
     */
    public void loadGameFile() throws IOException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Save File");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Sokoban save file", "*.skb"));
        saveFile = fileChooser.showOpenDialog(primaryStage);

        if (saveFile != null) {

            gameEngine.getMusic().stopMusic();
            if (StartMeUp.isDebugActive()) {
                StartMeUp.logger.info("Loading save file: " + saveFile.getName());
            }
            FileInputStream fileInputStream = new FileInputStream(saveFile);
            initializeGame(fileInputStream);
            fileInputStream.close();
        }
        saveFile = null;
    }

    /**
     * Save the status of current game and put it to a file.
     * @throws IOException  IO is not available
     */
    public void saveGameFile() throws IOException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save File");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Sokoban save file", "*.skb"));
        saveFile = fileChooser.showSaveDialog(primaryStage);
        if (!saveFile.exists()) {
            saveFile.createNewFile();
        }
        List<Level> levels = gameEngine.getLevels();
        GameGrid tempGrid;
        FileWriter fileWriter = new FileWriter(saveFile);
        BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
        bufferedWriter.write("MapSetName: " + gameEngine.getMapSetName()
                + System.lineSeparator());
        for (int i = 0; i < gameEngine.getLevelNum(); i ++) {
            tempGrid = gameEngine.getGrid(levels.get(i));
            bufferedWriter.write("LevelName: " + levels.get(i).getName()
                    + System.lineSeparator() + tempGrid.toString() + System.lineSeparator());
        }
        bufferedWriter.close();
        fileWriter.close();
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Saved Successfully");
        alert.setContentText("You have saved the file");
        alert.showAndWait();
        saveFile = null;
    }

    /**
     * Save the level scores (time and moves) of the current player to a file.
     * @throws IOException      IO is not available
     */
    public void saveLevelScore() throws IOException {
        String filepath;
        int time = (int) ((gameEngine.getTimeEnd()
                - gameEngine.getTimeStart()) / TIME_CAST);
        if (gameEngine.getCurrentLevel() == null) {
            filepath = "src/main/resources/score_lists/level" + gameEngine.getLevelNum() + ".txt";
        } else {
            filepath = "src/main/resources/score_lists/level" +
                    gameEngine.getCurrentLevel().getIndex() + ".txt";
        }
        File file = new File(filepath);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        FileReader fileReader = new FileReader(file);
        BufferedReader br = new BufferedReader(fileReader);

        String newline = name + "\t\t" + gameEngine.getCurrentMoves() + "\t\t"
                + time + System.lineSeparator();
        StringBuilder newElement = new StringBuilder();
        String lineContent;
        boolean inserted = false;
        while ((lineContent = br.readLine()) != null && !lineContent.equals("")) {
            if (!inserted) {
                String[] arr = lineContent.split("\\s+");
                if (Integer.parseInt(arr[arr.length - 1]) > time) {
                    newElement.append(newline);
                    newElement.append(lineContent);
                    newElement.append(System.lineSeparator());
                    inserted = true;
                } else if (Integer.parseInt(arr[arr.length - 1]) < time) {
                    newElement.append(lineContent);
                    newElement.append(System.lineSeparator());
                } else if (Integer.parseInt(arr[arr.length - 1]) == time) {
                    if (Integer.parseInt(arr[arr.length - 2]) > gameEngine.getCurrentMoves()) {
                        newElement.append(newline);
                        newElement.append(lineContent);
                        newElement.append(System.lineSeparator());
                        inserted = true;
                    } else if (Integer.parseInt(arr[arr.length-2]) <= gameEngine.getCurrentMoves()){
                        newElement.append(lineContent);
                        newElement.append(System.lineSeparator());
                    }
                }
            } else {
                newElement.append(lineContent);
                newElement.append(System.lineSeparator());
            }
        }
        if (file.length() == 0 || !inserted) {
            newElement.append(newline);
            newElement.append(System.lineSeparator());
        }

        br.close();
        fileReader.close();
        FileWriter fw = new FileWriter(file);
        BufferedWriter document = new BufferedWriter(fw);
        document.write(String.valueOf(newElement));
        document.flush();
        document.close();
        fw.close();

    }

    /**
     * Refresh the grid to display the change of each step.
     */
    public void reloadGrid() {
        if (gameEngine.isLevelComplete()) {
            try {
                showLevelMessage();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            try {
                saveLevelScore();
            } catch (IOException e) {
                e.printStackTrace();
            }
            gameEngine.setLevelComplete(false);
            gameEngine.setTimeStart(System.currentTimeMillis());
            gameEngine.setCurrentMoves(0);
        }

        if (gameEngine.isGameComplete()) {
            showVictoryMessage();
            return;
        }

        Level currentLevel = gameEngine.getCurrentLevel();
        Level.LevelIterator levelGridIterator = (Level.LevelIterator) currentLevel.iterator();
        gameGrid.getChildren().clear();
        while (levelGridIterator.hasNext()) {
            addObjectToGrid(levelGridIterator.next(), levelGridIterator.getCurrentPosition());
        }
        gameGrid.autosize();
        primaryStage.sizeToScene();
    }

    /**
     * Show a dialog message when finish all the levels in the game
     * to show the total steps and time.
     */
    public void showVictoryMessage() {
        String dialogTitle = "Game Over!";
        String dialogMessage = "You completed " + gameEngine.getMapSetName() + " in "
                + gameEngine.getMovesCount() + " moves and " +
                +gameEngine.getTimeCount() + "s";
        final int angle = 3;
        final int radius = 10;
        MotionBlur mb = new MotionBlur(angle, radius);

        dialogWindow.newDialog(dialogTitle, dialogMessage, mb);
    }

    /**
     * Show a dialog message when finish the current level to show the steps and time
     * in this level and also display the top 10 of current level.
     * @throws FileNotFoundException    Cannot get the file
     */
    public void showLevelMessage() throws FileNotFoundException {
        String dialogTitle = "Level Over!";
        StringBuilder dialogMessage = new StringBuilder();
        String playerMessage;
        if (gameEngine.getCurrentLevel() == null) {
            playerMessage = "You completed Final Level in "
                    + gameEngine.getCurrentMoves() + " moves!" + System.lineSeparator()
                    + "Total moves: " + gameEngine.getMovesCount() +System.lineSeparator() +
                    "Time: " + ((gameEngine.getTimeEnd() - gameEngine.getTimeStart()) / TIME_CAST)
                    + "s";
        } else {
            playerMessage = "You completed Level " + gameEngine.getCurrentLevel().getIndex()
                    + " in " + gameEngine.getCurrentMoves() + " moves!" + System.lineSeparator()
                     + "Total moves: " + gameEngine.getMovesCount() + System.lineSeparator() +
                    "Time: " + ((gameEngine.getTimeEnd() - gameEngine.getTimeStart()) / TIME_CAST)
                    + "s";
        }
        String filepath;
        if (gameEngine.getCurrentLevel() == null) {
            filepath = "src/main/resources/score_lists/level" + gameEngine.getLevelNum() + ".txt";
        } else {
            filepath = "src/main/resources/score_lists/level" +
                    gameEngine.getCurrentLevel().getIndex() + ".txt";
        }
        File file = new File(filepath);
        if (!file.exists() || file.length() == 0) {
            dialogMessage.append(playerMessage).append(System.lineSeparator())
                    .append(System.lineSeparator())
                    .append(System.lineSeparator()).append("No Records");
        } else {
            dialogMessage.append(playerMessage)
                    .append(System.lineSeparator()).append(System.lineSeparator())
                    .append("Top 10 for this level").append(System.lineSeparator())
                    .append(System.lineSeparator()).append("Name" + "\t" + "Moves" + "\t" + "Time")
                    .append(System.lineSeparator()).append(System.lineSeparator());
            int count = 0;
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                dialogMessage.append(scanner.nextLine()).append(System.lineSeparator());
                count++;
                if (count == TOP_TEN) {
                    break;
                }
            }
            scanner.close();
        }
        dialogWindow.newDialog(dialogTitle, dialogMessage.toString(), null);
    }

    /**
     * Show the top 10 scores of current level in a dialog message.
     * @param levelNumber   Index of level
     * @throws FileNotFoundException    Cannot get the file
     */
    public void showScoreList(int levelNumber) throws FileNotFoundException {
        String dialogTitle = "TOP 10";
        StringBuilder dialogMessage = new StringBuilder();
        String filepath = "src/main/resources/score_lists/level" +
                levelNumber + ".txt";
        File file = new File(filepath);
        if (!file.exists() || file.length() == 0) {
            dialogMessage.append("No Records");
        } else {
            dialogMessage.append("Top 10 for this level").append(System.lineSeparator())
                    .append(System.lineSeparator()).append("Name" + "\t" + "Moves" + "\t" + "Time")
                    .append(System.lineSeparator()).append(System.lineSeparator());
            int count = 0;
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                dialogMessage.append(scanner.nextLine()).append(System.lineSeparator());
                count++;
                if (count == TOP_TEN) {
                    break;
                }
            }
            scanner.close();
        }
        dialogWindow.newDialog(dialogTitle, dialogMessage.toString(), null);
    }

    /**
     * Add the object to the specific position in the gameGrid.
     * @param gameObject    the object to add
     * @param location  the location for the object to add
     */
    public void addObjectToGrid(GameObject gameObject, Point location) {
        GraphicObject graphicObject = null;
        try {
            graphicObject = new GraphicObject(gameObject);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        gameGrid.add(graphicObject, location.y, location.x);
    }

    /**
     * Move the keeper to the previous position.
     */
    public void gotoLastLocation() {
        int size = gameEngine.getKeeperDirection().size();
        Point keeperPosition = gameEngine.getCurrentLevel().getKeeperPosition();
        GameObject keeper = gameEngine.getCurrentLevel().getObjectAt(keeperPosition);
        if (size != 0) {
            short lastDirection = gameEngine.getKeeperDirection().removeLast();
            String lastTarget = gameEngine.getTarget().removeLast();
            if (lastTarget.equals("FLOOR")) {
                switch (lastDirection) {
                    case D_UP:
                        gameEngine.getCurrentLevel().moveGameObjectBy(keeper, keeperPosition,
                                new Point(1, 0));
                        keeperPosition.translate(1, 0);
                        break;
                    case D_RIGHT:
                        gameEngine.getCurrentLevel().moveGameObjectBy(keeper, keeperPosition,
                                new Point(0, -1));
                        keeperPosition.translate(0, -1);
                        break;
                    case D_DOWN:
                        gameEngine.getCurrentLevel().moveGameObjectBy(keeper, keeperPosition,
                                new Point(-1, 0));
                        keeperPosition.translate(-1, 0);
                        break;
                    case D_LEFT:
                        gameEngine.getCurrentLevel().moveGameObjectBy(keeper, keeperPosition,
                                new Point(0, 1));
                        keeperPosition.translate(0, 1);
                        break;
                    default:
                        throw new AssertionError("This should not have happened. " +
                                "Report this problem to the developer.");
                }
            } else if (lastTarget.equals("CRATE")) {
                switch (lastDirection) {
                    case D_UP:
                        Point targetObjectPoint0 =
                                GameGrid.translatePoint(keeperPosition, new Point(-1, 0));
                        GameObject keeperTarget0 =
                                gameEngine.getCurrentLevel().getObjectAt(targetObjectPoint0);
                        gameEngine.getCurrentLevel().moveGameObjectBy(keeper, keeperPosition,
                                new Point(1, 0));
                        gameEngine.getCurrentLevel().moveGameObjectBy(keeperTarget0, targetObjectPoint0,
                                new Point(1, 0));
                        keeperPosition.translate(1, 0);
                        break;
                    case D_RIGHT:
                        Point targetObjectPoint1 =
                                GameGrid.translatePoint(keeperPosition, new Point(0, 1));
                        GameObject keeperTarget1 =
                                gameEngine.getCurrentLevel().getObjectAt(targetObjectPoint1);
                        gameEngine.getCurrentLevel().moveGameObjectBy(keeper, keeperPosition,
                                new Point(0, -1));
                        gameEngine.getCurrentLevel().moveGameObjectBy(keeperTarget1, targetObjectPoint1,
                                new Point(0, -1));
                        keeperPosition.translate(0, -1);
                        break;
                    case D_DOWN:
                        Point targetObjectPoint2 =
                                GameGrid.translatePoint(keeperPosition, new Point(1, 0));
                        GameObject keeperTarget2 =
                                gameEngine.getCurrentLevel().getObjectAt(targetObjectPoint2);
                        gameEngine.getCurrentLevel().moveGameObjectBy(keeper, keeperPosition,
                                new Point(-1, 0));
                        gameEngine.getCurrentLevel().moveGameObjectBy(keeperTarget2, targetObjectPoint2,
                                new Point(-1, 0));
                        keeperPosition.translate(-1, 0);
                        break;
                    case D_LEFT:
                        Point targetObjectPoint3 =
                                GameGrid.translatePoint(keeperPosition, new Point(0, -1));
                        GameObject keeperTarget3 =
                                gameEngine.getCurrentLevel().getObjectAt(targetObjectPoint3);
                        gameEngine.getCurrentLevel().moveGameObjectBy(keeper, keeperPosition,
                                new Point(0, 1));
                        gameEngine.getCurrentLevel().moveGameObjectBy(keeperTarget3, targetObjectPoint3,
                                new Point(0, 1));
                        keeperPosition.translate(0, 1);
                        break;
                    default:
                        throw new AssertionError("This should not have happened. " +
                                "Report this problem to the developer.");
                }
            }
            // penalty for using undo
            gameEngine.setMovesCount(gameEngine.getMovesCount() + 1);
        }
    }
}
