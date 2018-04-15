package ru.spbau.mit.java.paradov.controllers;

import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Toggle;
import javafx.stage.Stage;
import ru.spbau.mit.java.paradov.util.UtilFunctions;

import java.io.IOException;

/**
 * Controller for game settings menu and its layout.
 */
public class GameSetController {
    /**
     * Event handler for pressing GoBack button. Creates new scene for main menu layout.
     * @param actionEvent event of pressing GoBack button
     * @throws IOException if there is no main menu layout
     */
    public void backToMenu(ActionEvent actionEvent) throws IOException {
        Stage stage = UtilFunctions.getStageFromButtonActionEvent(actionEvent);
        UtilFunctions.setStage(getClass(), stage, "/MainMenuLayout.fxml");
    }

    /**
     * Event handler for pressing BeginGame button. Creates new scene for game field layout.
     * @param actionEvent event of pressing BeginGame button
     * @throws IOException if there is no game field layout
     */
    public void beginGame(ActionEvent actionEvent) throws IOException {
        Stage stage = UtilFunctions.getStageFromButtonActionEvent(actionEvent);
        Scene scene = stage.getScene();

        Toggle p1 = (Toggle) scene.lookup("#Player1Human");
        String player1 = ((RadioButton) p1.getToggleGroup().getSelectedToggle()).getId();

        Toggle p2 = (Toggle) scene.lookup("#Player2Human");
        String player2 = ((RadioButton) p2.getToggleGroup().getSelectedToggle()).getId();

        UtilFunctions.setStage(getClass(), stage, "/GameFieldLayout.fxml");

        GameFieldController.setupGame(stage, player1, player2);
    }
}
