package com.sokoban.controller;

import com.sokoban.model.StageModel;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * The class MainPageController controls actions
 * on the menu of main game page.
 * @author Yuyang LIN
 */
public class MainPageController {

    private StageModel model;

    /**
     * Constructor of MainPageController to initialize StageModel.
     * @param model     stage model
     */
    public MainPageController(StageModel model) {
        this.model = model;
    }

    /**
     * Closes the game and exit when
     * click the button in menu {@code File -> Exit}.
     */
    public void closeGame() {
        System.exit(0);
    }

    /**
     * Save the current level and status to a .skb file when
     * click the button in menu {@code File -> Save Game}.
     */
    public void saveGame() {
        try {
            model.saveGameFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Load a .skb file when click the button in menu {@code File -> Load Game}.
     */
    public void loadGame() {
        try {
            model.loadGameFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Execute the undo operation when
     * click the button in menu {@code Level -> Undo}.
     */
    public void undo() {
        model.gotoLastLocation();
        model.reloadGrid();
    }

    /**
     * Reset current level to the beginning when
     * click the button in menu {@code Level -> Reset Level}.
     */
    public void resetLevel() {
        int size = model.getGameEngine().getKeeperDirection().size();
        while (size >= 0) {
            model.gotoLastLocation();
            size--;
        }
        model.reloadGrid();
    }

    /**
     * Show something about the game when
     * click the button in menu {@code About -> About This Game}.
     */
    public void showAbout() {
        String title = "About This Game";
        String message = "Enjoy the Game!\n";

        model.getDialogWindow().newDialog(title, message, null);
    }

    /**
     * Stop / Start the music when
     * click the button in menu {@code Level -> Toggle Music}.
     */
    public void toggleMusic() {
        if (model.getGameEngine().getMusic().isPlayingMusic()) {
            model.getGameEngine().getMusic().stopMusic();
        } else {
            model.getGameEngine().getMusic().playMusic();
        }

    }

    /**
     * Turn on / off the debug mode when
     * click the button in menu {@code About -> Toggle Debug}.
     */
    public void toggleDebug() {
        model.getGameEngine().toggleDebug();
        model.reloadGrid();
    }

    /**
     * Show the top 10 scores of the current level when
     * click the button in menu {@code Score -> Top 10 High Scores}.
     */
    public void showScores() {
        try {
            model.showScoreList(model.getGameEngine().getCurrentLevel().getIndex() + 1);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}


