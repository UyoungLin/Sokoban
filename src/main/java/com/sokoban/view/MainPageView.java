package com.sokoban.view;

import com.sokoban.model.StageModel;
import com.sokoban.controller.MainPageController;
import com.sokoban.game.StartMeUp;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import java.io.IOException;

/**
 * The class MainPageView stores all the items that will be displayed on the scene.
 * @author Yuyang LIN
 */
public class MainPageView {
    private MainPageController controller;
    private StageModel model;
    private Stage primaryStage;
    private GridPane mainPage;
    private GridPane gameGrid;
    private Scene scene;
    private MenuBar MENU;
    private MenuItem menuItemSaveGame;
    private MenuItem menuItemLoadGame;
    private MenuItem menuItemExit;
    private MenuItem menuItemUndo;
    private MenuItem menuItemResetLevel;
    private MenuItem menuItemGame;
    private MenuItem menuItemHighScores;
    private RadioMenuItem radioMenuItemMusic;
    private RadioMenuItem radioMenuItemDebug;
    private Menu menuFile;
    private Menu menuAbout;
    private Menu menuLevel;
    private Menu menuScore;
    private GridPane display;
    private Label movesCount;
    private Label timeCount;
    private Label levelDisplay;
    private final int PRE_HEIGHT = 20;

    /**
     * Set the text to indicate the playing time.
     * @param label     the string to put in the label
     */
    public void setTimeCountText(String label) {
        timeCount.setText(label);
    }

    /**
     * Set the text to indicate the moves count.
     * @param label     the string to put in the label
     */
    public void setMovesCountText(String label) {
        movesCount.setText(label);
    }

    /**
     * Set the text to indicate the current level.
     * @param label     the string to put in the label
     */
    public void setLevelDisplay(String label) {
        levelDisplay.setText(label);
    }

    /**
     * Constructor to setup the basic elements of the main page.
     * @param controller    main page controller
     * @param model     stage model
     * @param primaryStage  primary stage
     */
    public MainPageView(MainPageController controller, StageModel model, Stage primaryStage) {
        this.controller = controller;
        this.model = model;
        this.primaryStage = primaryStage;
        createAndConfigurePane();
        createGameScene();
        updateController();
    }

    /**
     * Make the pane as a parent node.
     * @return {@code mainPage}
     */
    public Parent asParent() {
        return mainPage;
    }

    /**
     * To make the scene displayed.
     * @throws IOException      IO is not available
     */
    public void setNewScene() throws IOException {
        scene = new Scene(this.asParent());
        this.primaryStage.setTitle(StartMeUp.GAME_NAME);
        this.primaryStage.setScene(scene);
        this.primaryStage.show();
        this.model.loadDefaultSaveFile(primaryStage);
    }

    /**
     * Set the basic pane.
     */
    private void createAndConfigurePane() {
        mainPage = new GridPane();
        display = new GridPane();
        display.setStyle("-fx-pref-height: " + PRE_HEIGHT + ";");
        display.setAlignment(Pos.CENTER);
        gameGrid = model.getGameGrid();
    }

    /**
     * Create and put all the elements in the grid pane.
     */
    public void createGameScene() {
        MENU = new MenuBar();
        menuItemSaveGame = new MenuItem("Save Game");
        menuItemLoadGame = new MenuItem("Load Game");
        menuItemExit = new MenuItem("Exit");
        menuFile = new Menu("File");
        menuFile.getItems().addAll(menuItemSaveGame, menuItemLoadGame,
                new SeparatorMenuItem(),menuItemExit);
        menuItemUndo = new MenuItem("Undo");
        radioMenuItemMusic = new RadioMenuItem("Toggle Music");
        radioMenuItemDebug = new RadioMenuItem("Toggle Debug");
        menuItemResetLevel = new MenuItem("Reset Level");
        menuLevel = new Menu("Level");
        menuLevel.getItems().addAll(menuItemUndo, radioMenuItemMusic,
                radioMenuItemDebug, new SeparatorMenuItem(),menuItemResetLevel);
        menuItemGame = new MenuItem("About This Game");
        menuAbout = new Menu("About");
        menuAbout.getItems().addAll(menuItemGame);
        menuItemHighScores = new MenuItem("Top 10 High Scores");
        menuScore = new Menu("Score");
        menuScore.getItems().addAll(menuItemHighScores);
        MENU.getMenus().addAll(menuFile, menuLevel, menuAbout, menuScore);
        movesCount = new Label();
        timeCount = new Label();
        levelDisplay = new Label();
        display.addRow(0, movesCount, timeCount, levelDisplay);
        mainPage.add(MENU,0,0);
        mainPage.add(display,0,1);
        mainPage.add(gameGrid,0,2);
    }

    /**
     * Set the actions for the items.
     */
    private void updateController() {
        menuItemSaveGame.setOnAction(actionEvent ->controller.saveGame());
        menuItemLoadGame.setOnAction(actionEvent ->controller.loadGame());
        menuItemExit.setOnAction(actionEvent ->controller.closeGame());
        menuItemUndo.setOnAction(actionEvent ->controller.undo());
        radioMenuItemMusic.setOnAction(actionEvent ->controller.toggleMusic());
        radioMenuItemDebug.setOnAction(actionEvent ->controller.toggleDebug());
        menuItemResetLevel.setOnAction(actionEvent ->controller.resetLevel());
        menuItemGame.setOnAction(actionEvent ->controller.showAbout());
        menuItemHighScores.setOnAction(actionEvent ->controller.showScores());
    }
}
