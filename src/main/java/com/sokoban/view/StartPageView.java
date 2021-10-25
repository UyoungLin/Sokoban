package com.sokoban.view;

import com.sokoban.model.StageModel;
import com.sokoban.game.StartMeUp;
import com.sokoban.controller.StartPageController;
import javafx.collections.FXCollections;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import java.io.IOException;

/**
 * The class StartPageView stores all the items that will be displayed in the start page.
 * @author Yuyang LIN
 */
public class StartPageView {
    private StartPageController controller;
    private StageModel model;
    private Stage primaryStage;
    private AnchorPane start;
    private Scene scene;
    private Button[] buttons = new Button[3];
    private TextField textField;
    private Label name;
    private Label choose;
    private Label wallColour;
    private ChoiceBox<String> choiceBox;
    private final int SCREEN_LENGTH = 600;
    private final int S_FONT_SIZE = 15;
    private final int M_FONT_SIZE = 17;
    private final int L_FONT_SIZE = 26;
    private final int LAYOUT_X1 = 340;
    private final int LAYOUT_X2 = 486;
    private final int LAYOUT_X3 = 139;
    private final int LAYOUT_X4 = 53;
    private final int LAYOUT_Y1 = 490;
    private final int LAYOUT_Y2 = 555;
    private final int LAYOUT_Y3 = 496;
    private final int LAYOUT_Y4 = 515;
    private final int PRE_HEIGHT1 = 32;
    private final int PRE_HEIGHT2 = 34;
    private final int PRE_HEIGHT3 = 19;
    private final int PRE_HEIGHT4 = 50;
    private final int PRE_WIDTH1 = 206;
    private final int PRE_WIDTH2 = 127;
    private final int PRE_WIDTH3 = 60;
    private final int PRE_WIDTH4 = 145;
    private final int PRE_WIDTH5 = 81;

    /**
     * Constructor to setup the basic elements of the start page.
     * @param controller    start page controller
     * @param model     stage model
     * @param primaryStage  primary stage
     */
    public StartPageView(StartPageController controller, StageModel model, Stage primaryStage) {
        this.controller = controller;
        this.model = model;
        this.primaryStage = primaryStage;
        createAndConfigurePane();
        createAndLayoutControls();
        updateController();
        scene = new Scene(this.asParent(), SCREEN_LENGTH, SCREEN_LENGTH);
        this.primaryStage.setTitle(StartMeUp.GAME_NAME);
        this.primaryStage.setScene(scene);
        this.primaryStage.show();
    }

    /**
     * Make the pane as a parent node.
     * @return {@code start}
     */
    public Parent asParent() {
        return start;
    }

    /**
     * Initialize the anchor pane.
     */
    private void createAndConfigurePane() {
        start = new AnchorPane();
    }

    /**
     * Create and put all the elements in the pane.
     */
    private void createAndLayoutControls() {
        buttons[0]=new Button("Start");
        buttons[1]=new Button("High Scores");
        buttons[2]=new Button("Exit");
        textField = new TextField();
        name = new Label("Player Name");
        choose = new Label("Choose");
        wallColour = new Label("Wall Colour");
        choiceBox = new ChoiceBox<>(FXCollections.observableArrayList("Yellow", "Black",
                "Brown", "Grey"));

        buttons[0].setLayoutX(LAYOUT_X1);
        buttons[0].setLayoutY(LAYOUT_Y1);
        buttons[0].setStyle("-fx-font-size: " + L_FONT_SIZE +
                "; -fx-pref-width: " + PRE_WIDTH1 + ";");
        buttons[1].setLayoutX(LAYOUT_X1);
        buttons[1].setLayoutY(LAYOUT_Y2);
        buttons[1].prefHeight(PRE_HEIGHT1);
        buttons[1].setStyle("-fx-font-size: " + M_FONT_SIZE +
                "; -fx-pref-width: " + PRE_WIDTH2 + ";");
        buttons[2].setLayoutX(LAYOUT_X2);
        buttons[2].setLayoutY(LAYOUT_Y2);
        buttons[2].prefHeight(PRE_HEIGHT1);
        buttons[2].setStyle("-fx-font-size: " + M_FONT_SIZE +
                "; -fx-pref-width: " + PRE_WIDTH3 + ";");
        textField.setLayoutX(LAYOUT_X3);
        textField.setLayoutY(LAYOUT_Y2);
        textField.setStyle("-fx-pref-width: " + PRE_WIDTH4 +
                "; -fx-pref-height: " + PRE_HEIGHT1 + ";");

        name.setLayoutX(LAYOUT_X4);
        name.setLayoutY(LAYOUT_Y2);
        name.prefHeight(PRE_HEIGHT2);
        name.setStyle("-fx-font-size: " + S_FONT_SIZE +
                "; -fx-pref-width: " + PRE_WIDTH5 + ";");

        choose.setLayoutX(LAYOUT_X4);
        choose.setLayoutY(LAYOUT_Y3);
        choose.prefHeight(PRE_HEIGHT3);
        choose.setStyle("-fx-font-size: " + S_FONT_SIZE +
                "; -fx-pref-width: " + PRE_WIDTH5 + ";");
        wallColour.setLayoutX(LAYOUT_X4);
        wallColour.setLayoutY(LAYOUT_Y4);
        wallColour.prefHeight(PRE_HEIGHT3);
        wallColour.setStyle("-fx-font-size: " + S_FONT_SIZE +
                "; -fx-pref-width: " + PRE_WIDTH5 + ";");
        choiceBox.setLayoutX(LAYOUT_X3);
        choiceBox.setLayoutY(LAYOUT_Y1);
        choiceBox.setStyle("-fx-pref-width: " + PRE_WIDTH4 +
                "; -fx-pref-height: " + PRE_HEIGHT4 + ";");
        choiceBox.setValue("Black");

        start.getChildren().add(buttons[0]);
        start.getChildren().add(buttons[1]);
        start.getChildren().add(buttons[2]);
        start.getChildren().add(textField);
        start.getChildren().add(name);
        start.getChildren().add(choose);
        start.getChildren().add(wallColour);
        start.getChildren().add(choiceBox);

        start.setStyle("-fx-background-image: url('bg.png');");
    }

    /**
     * Set the actions for the items.
     */
    private void updateController() {
        buttons[0].setOnMouseReleased(e->{
            try {
                controller.startGame();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        });
        buttons[1].setOnAction(e->{controller.showHighScores();});
        buttons[2].setOnAction(e->{controller.closeApp();});

        textField.setOnMouseExited(e->{
            model.setName(textField.getText());
        });

        choiceBox.setOnAction(e->{
            String choice = choiceBox.getValue().toString();
            switch (choice) {
                case "Yellow":
                    StageModel.wallColour = "Yellow";
                    break;
                case "Brown":
                    StageModel.wallColour = "Brown";
                    break;
                case "Grey":
                    StageModel.wallColour = "Grey";;
                    break;
                default:
                    StageModel.wallColour = "Black";
                    break;
            }
        });
    }
}
