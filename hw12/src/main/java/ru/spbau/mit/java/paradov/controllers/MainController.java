package ru.spbau.mit.java.paradov.controllers;

import javafx.event.ActionEvent;
import javafx.stage.Stage;

import ru.spbau.mit.java.paradov.util.UtilFunctions;


public class MainController {
    public void closeWindow(ActionEvent actionEvent) {
        UtilFunctions.getStageFromButtonActionEvent(actionEvent).close();
    }

    public void showStats(ActionEvent actionEvent) throws Exception {
        Stage stage = UtilFunctions.getStageFromButtonActionEvent(actionEvent);
        UtilFunctions.setStage(getClass(), stage, "StatsMenuLayout.fxml");

        StatsMenuController.setStatsScene(stage);
    }


    public void startGame(ActionEvent actionEvent) throws Exception {
        Stage stage = UtilFunctions.getStageFromButtonActionEvent(actionEvent);
        UtilFunctions.setStage(getClass(), stage, "GameSetLayout.fxml");
    }
}
