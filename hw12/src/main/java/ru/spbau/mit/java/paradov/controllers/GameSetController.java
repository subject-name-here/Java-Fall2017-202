package ru.spbau.mit.java.paradov.controllers;

import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Toggle;
import javafx.stage.Stage;
import ru.spbau.mit.java.paradov.logic.GameLogic;
import ru.spbau.mit.java.paradov.util.UtilFunctions;

public class GameSetController {
    public void backToMenu(ActionEvent actionEvent) throws Exception {
        Stage stage = UtilFunctions.getStageFromButtonActionEvent(actionEvent);
        UtilFunctions.setStage(getClass(), stage, "MainMenuLayout.fxml");
    }

    public void beginGame(ActionEvent actionEvent) throws Exception {
        Stage stage = UtilFunctions.getStageFromButtonActionEvent(actionEvent);
        Scene scene = stage.getScene();

        Toggle p1 = (Toggle) scene.lookup("#Player1Human");
        String player1 = ((RadioButton) p1.getToggleGroup().getSelectedToggle()).getId();

        Toggle p2 = (Toggle) scene.lookup("#Player2Human");
        String player2 = ((RadioButton) p2.getToggleGroup().getSelectedToggle()).getId();

        UtilFunctions.setStage(getClass(), stage, "GameFieldLayout.fxml");

        GameFieldController.setupField(stage);
        GameLogic.beginGame(stage, player1, player2);
    }
}
