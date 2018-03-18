package ru.spbau.mit.java.paradov.controllers;

import javafx.event.ActionEvent;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import ru.spbau.mit.java.paradov.data.Stats;
import ru.spbau.mit.java.paradov.util.UtilFunctions;


public class StatsMenuController {
    public static void setStatsScene(Stage stage) {
        ((Label) stage.getScene().lookup("#XWins")).setText(Stats.getXWins().toString());
        ((Label) stage.getScene().lookup("#OWins")).setText(Stats.getOWins().toString());
        ((Label) stage.getScene().lookup("#DrawsPvP")).setText(Stats.getDrawsPvP().toString());

        ((Label) stage.getScene().lookup("#HumanWins")).setText(Stats.getHumanWins().toString());
        ((Label) stage.getScene().lookup("#BotWins")).setText(Stats.getBotWins().toString());
        ((Label) stage.getScene().lookup("#DrawsPvE")).setText(Stats.getDrawsPvE().toString());
    }

    public void returnToMenu(ActionEvent actionEvent) throws Exception {
        Stage stage = UtilFunctions.getStageFromButtonActionEvent(actionEvent);
        UtilFunctions.setStage(getClass(), stage, "MainMenuLayout.fxml");
    }
}
