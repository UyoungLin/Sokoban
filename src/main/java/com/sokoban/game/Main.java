package com.sokoban.game;

import com.sokoban.controller.MainPageController;
import com.sokoban.controller.StartPageController;
import com.sokoban.model.StageModel;
import com.sokoban.view.MainPageView;
import com.sokoban.view.StartPageView;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 * The class Main is the place to load and start the mvc patterns.
 * @author Yuyang LIN
 */
public class Main extends Application {

    /**
     * The main method.
     *
     * @param args - the arguments
     */
    public static void main(String[] args) {
        launch(args);
        System.out.println("Done!");
    }

    /**
     * Generate the start scene.
     * @param primaryStage  Primary stage
     * @throws Exception the exception
     */
    @Override
    public void start(Stage primaryStage) throws Exception {

        StageModel stageModel = new StageModel();
        MainPageController mainPageController = new MainPageController(stageModel);
        MainPageView mainPageView =
                new MainPageView(mainPageController, stageModel, primaryStage);
        StartPageController startPageController =
                new StartPageController(stageModel, mainPageView);
        StartPageView startPageView =
                new StartPageView(startPageController, stageModel, primaryStage);

    }

}
