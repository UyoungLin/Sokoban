package com.sokoban.game;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.effect.Effect;
import javafx.scene.layout.Background;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * The class Dialog set up a dialog to make some dialog messages.
 * @author Yuyang LIN
 */
public class Dialog {

    private Stage primaryStage;
    private GridPane gameGrid;
    private Text text1;
    private VBox dialogVbox;
    private Scene dialogScene;
    private final int SPACING = 20;
    private final int FONT = 14;
    private final int SCENE_WIDTH = 500;
    private final int SCENE_HEIGHT = 400;

    /**
     * Constructor of Dialog to initialize primaryStage and gameGrid.
     * @param primaryStage  Primary stage
     * @param gameGrid      Grid for game
     */
    public Dialog(Stage primaryStage, GridPane gameGrid) {
        this.primaryStage = primaryStage;
        this.gameGrid = gameGrid;
    }

    /**
     * Set up a dialog window with title message and effect.
     * @param dialogTitle   the title of the dialog window
     * @param dialogMessage     the message that will be shown in the dialog window
     * @param dialogMessageEffect   the effects on gameGrid
     */
    public void newDialog(String dialogTitle, String dialogMessage, Effect dialogMessageEffect) {
        final Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initOwner(primaryStage);
        dialog.setResizable(false);
        dialog.setTitle(dialogTitle);
        text1 = new Text(dialogMessage);
        text1.setTextAlignment(TextAlignment.CENTER);
        text1.setFont(javafx.scene.text.Font.font(FONT));

        if (dialogMessageEffect != null) {
            gameGrid.setEffect(dialogMessageEffect);
        }

        dialogVbox = new VBox(SPACING);
        dialogVbox.setAlignment(Pos.CENTER);
        dialogVbox.setBackground(Background.EMPTY);
        dialogVbox.getChildren().add(text1);
        dialogScene = new Scene(dialogVbox, SCENE_WIDTH, SCENE_HEIGHT);
        dialog.setScene(dialogScene);
        dialog.showAndWait();
    }
}
