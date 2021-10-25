package com.sokoban.controller;

import com.sokoban.model.StageModel;
import com.sokoban.game.Level;
import com.sokoban.view.MainPageView;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.*;
import javafx.util.Duration;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.NoSuchElementException;
import java.util.Optional;

/**
 * The class StartPageController controls actions
 * of the buttons in start page.
 * @author Yuyang LIN
 */
public class StartPageController {
    private StageModel model;
    private MainPageView mainPageView;
    private Timeline animationMove;
    private Timeline animationTime;
    private Timeline animationLevel;
    private final int DURATION = 1000;

    private int index = 1;

    /**
     * Constructor of StartPageController to initialize StageModel and MainPageView.
     * @param model     stage model
     * @param mainPageView      instance of main page view
     */
    public StartPageController(StageModel model, MainPageView mainPageView) {
        this.model = model;
        this.mainPageView = mainPageView;
    }

    /**
     * Start the game when click the button {@code Start}.
     * @throws IOException      If cannot get file
     */
    public void startGame() throws IOException {
        if (model.getName().equals("")) {
            Alert noName = new Alert(Alert.AlertType.WARNING);
            noName.setTitle("Warning");
            noName.setHeaderText("Oops");
            noName.setContentText("Please enter your name!");
            noName.showAndWait();
        } else {
            mainPageView.setNewScene();

            EventHandler<ActionEvent> movesHandler = e -> {
                int move = model.getGameEngine().getMovesCount();
                mainPageView.setMovesCountText("Moves: " + move + "\t\t\t");
            };

            animationMove = new Timeline(new KeyFrame(Duration.millis(DURATION), movesHandler));
            animationMove.setCycleCount(Animation.INDEFINITE);
            animationMove.play();

            DateFormat time = new SimpleDateFormat("mm:ss");

            EventHandler<ActionEvent> timeHandler = e -> {
                long timeScope = System.currentTimeMillis()
                        - model.getGameEngine().getTimeStart();
                if (timeScope > 0) {
                    mainPageView.setTimeCountText("Time: " + time.format(timeScope) + "\t\t\t");
                } else {
                    mainPageView.setTimeCountText("Time: " + time.format(0) + "\t\t\t");
                }
            };
            animationTime = new Timeline(new KeyFrame(Duration.millis(DURATION), timeHandler));
            animationTime.setCycleCount(Animation.INDEFINITE);
            animationTime.play();

            EventHandler<ActionEvent> levelHandler = e -> {
                Level tempLevel = model.getGameEngine().getCurrentLevel();
                if (tempLevel != null) {
                    mainPageView.setLevelDisplay("Level: " + (tempLevel.getIndex() + 1) + "\t\t\t");
                } else {
                    mainPageView.setLevelDisplay("Completed All Levels");
                }

            };
            animationLevel = new Timeline(new KeyFrame(Duration.millis(DURATION), levelHandler));
            animationLevel.setCycleCount(Animation.INDEFINITE);
            animationLevel.play();
        }
    }

    /**
     * Closes the game and exit when
     * click the button in menu {@code Exit}.
     */
    public void closeApp() {
        System.exit(0);
    }

    /**
     * Show a choice box for user to choose a level to have a view of
     * and record the result of user.
     */
    public void showChoiceBox() {
        final ChoiceDialog<String> choiceDialog =
                new ChoiceDialog<>("1", "2", "3", "4", "5", "6");
        choiceDialog.setTitle("Choose a Level");
        choiceDialog.setHeaderText("");
        final Optional<String> opt = choiceDialog.showAndWait();
        try{
            opt.ifPresent(s -> index = Integer.parseInt(s));
        } catch (final NoSuchElementException e){
            e.printStackTrace();
        }
    }

    /**
     * Show a choice box for user to choose a level and show
     * the top 10 of the chosen level when click the button {@code High Scores}.
     */
    public void showHighScores() {
        try {
            showChoiceBox();
            model.showScoreList(index);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }
}