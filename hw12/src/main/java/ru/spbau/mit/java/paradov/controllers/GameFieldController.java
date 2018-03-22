package ru.spbau.mit.java.paradov.controllers;

import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import ru.spbau.mit.java.paradov.data.GameResult;
import ru.spbau.mit.java.paradov.data.Stats;
import ru.spbau.mit.java.paradov.logic.GameLogic;
import ru.spbau.mit.java.paradov.util.UtilFunctions;

import java.io.IOException;

/**
 * Controller for game field layout.
 */
public class GameFieldController {
    /** Game that is being played on this layout. */
    private static GameLogic game;
    /** Stage where this layout placed. */
    private static Stage currentStage;
    /** All buttons on the field. */
    private static Button[][] buttonField;

    public static void setupGame(Stage stage, String p1, String p2) {
        currentStage = stage;
        buttonField = new Button[GameLogic.ROWS][GameLogic.COLUMNS];

        GridPane metaField = (GridPane) stage.getScene().lookup("#MetaField");
        GridPane field = new GridPane();
        field.setAlignment(Pos.CENTER);

        metaField.add(field, 0, 1);
        for (Integer i = 0; i < GameLogic.ROWS; i++) {
            for (Integer j = 0; j < GameLogic.COLUMNS; j++) {
                Button button = new Button();
                button.setText(" ");
                button.setId(i.toString() + " " + j.toString());
                button.prefWidthProperty().bind(field.widthProperty().divide(GameLogic.COLUMNS));
                button.prefHeightProperty().bind(field.heightProperty().divide(GameLogic.ROWS));
                button.setOnAction(GameFieldController::buttonPressed);

                // Coordinates are in wrong order, because it's said so in signature of method.
                field.add(button, j, i);
                buttonField[i][j] = button;
            }
        }

        game = new GameLogic(p1, p2);
        game.beginGame();
    }

    /**
     * Handles field button pressing event.
     * @param actionEvent event of pressing button
     */
    public static void buttonPressed(ActionEvent actionEvent) {
        Button button = (Button) actionEvent.getSource();
        String[] coordinates = button.getId().split(" ");
        Integer row = Integer.decode(coordinates[0]);
        Integer col = Integer.decode(coordinates[1]);

        game.buttonPressed(row, col);
    }

    /**
     * Changes label text according to whose turn it is.
     */
    public static void changeLabelTurn() {
        Label title = (Label) currentStage.getScene().lookup("#Turn");
        if (game.isXTurn()) {
            title.setText("X Move");
        } else {
            title.setText("O Move");
        }
    }

    /**
     * Changes button text according to whose turn it is.
     * @param r row where move was done
     * @param c column where move was done
     */
    public static void changeButton(Integer r, Integer c) {
        if (game.isXTurn()) {
            buttonField[r][c].setText("X");
        } else {
            buttonField[r][c].setText("O");
        }
    }

    /**
     * Handles finishing of the game: sets label and button text, saves result to stats.
     * @param result result of finished game
     */
    public static void finishGame(GameResult result) {
        Label title = (Label) currentStage.getScene().lookup("#Turn");

        if (result == GameResult.X_WON) {
            title.setText("X won!");
        } else if (result == GameResult.O_WON) {
            title.setText("O won!");
        } else if (result == GameResult.DRAW) {
            title.setText("Draw!");

        }

        Button exitButton = (Button) currentStage.getScene().lookup("#GoBack");
        exitButton.setText("Go Back");

        Stats.saveResult(game.getGameType(), result);
    }

    /**
     * Event handler for pressing GoBack button. Creates new scene for game settings layout.
     * @param actionEvent event of pressing GoBack button
     * @throws IOException if there is no game settings layout
     */
    public void goBack(ActionEvent actionEvent) throws IOException {
        UtilFunctions.setStage(getClass(), currentStage, "GameSetLayout.fxml");
    }
}
