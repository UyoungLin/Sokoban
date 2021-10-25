package com.sokoban.game;

import com.sokoban.model.StageModel;
import javafx.animation.FadeTransition;
import javafx.animation.Timeline;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import java.io.File;
import java.net.URISyntaxException;

/**
 * The class GraphicObject set all the details of the objects on the game grid.
 * @author Yuyang LIN-modified
 */
public class GraphicObject extends Rectangle {

    static int direction = 2;
    private final int D_UP = 0;
    private final int D_RIGHT = 1;
    private final int D_DOWN = 2;
    private final int D_LEFT = 3;
    private final int BLOCK_VAL = 30;
    private final double STROKE_WID = 0.25;
    private final int DURATION = 1000;
    private final double FROM = 1.0;
    private final double TO = 0.2;

    /**
     * Constructor to initialize the current object.
     * @param obj   Game object
     * @throws URISyntaxException   URI not found
     */
    public GraphicObject(GameObject obj) throws URISyntaxException {
        String element = "";

        switch (obj) {
            case WALL:
                element = setWall();
                break;

            case CRATE:
                element = "/images/box1.png";
                break;

            case DIAMOND:
                element = "/images/target1.png";

                if (StartMeUp.isDebugActive()) {
                    FadeTransition ft = new FadeTransition(Duration.millis(DURATION), this);
                    ft.setFromValue(FROM);
                    ft.setToValue(TO);
                    ft.setCycleCount(Timeline.INDEFINITE);
                    ft.setAutoReverse(true);
                    ft.play();
                }

                break;

            case KEEPER:
                element = setDirection();
                break;

            case FLOOR:
                element = "/images/ground3.png";
                break;

            case CRATE_ON_DIAMOND:
                element = "/images/box5.png";
                break;

            default:
                String message = "Error in Level constructor. Object not recognized.";
                StartMeUp.logger.severe(message);
                throw new AssertionError(message);
        }

        this.setHeight(BLOCK_VAL);
        this.setWidth(BLOCK_VAL);
        Image image = new Image(getClass().getResource(element).toURI().toString());
        this.setFill(new ImagePattern(image));

        if (StartMeUp.isDebugActive()) {
            this.setStroke(Color.RED);

            this.setStrokeWidth(STROKE_WID);
        }
    }

    /**
     * Get direction of the keeper object in StartMeUp.
     * @param directionEngine   the keeper object in StartMeUp
     */
    public static void giveDirection(short directionEngine) {
        direction = directionEngine;
    }

    /**
     * Set the image of keeper to have different direction.
     * @return String of file path of the keeper in different directions
     */
    public String setDirection() {
        String set;
        switch (direction) {
            case D_UP:
                set = "/images/mUP.png";
                break;
            case D_RIGHT:
                set = "/images/mRight.png";
                break;
            case D_DOWN:
                set = "/images/mDown.png";
                break;
            case D_LEFT:
                set = "/images/mLeft.png";
                break;
            default:
                set = "/images/mDown.png";
                break;
        }
        return set;

    }

    /**
     * Set the file path of the wall to have different wall colors.
     * @return String of file path of wall colours
     */
    public String setWall() {
        if (StageModel.wallColour == null) {
            return "/images/wall2.png";
        }
        switch (StageModel.wallColour) {
            case "Yellow":
                return "/images/wall1.png";

            case "Brown":
                return "/images/wall3.png";

            case "Grey":
                return "/images/wall4.png";

            default:
                return "/images/wall2.png";
        }
    }

}
