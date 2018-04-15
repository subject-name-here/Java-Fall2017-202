package ru.spbau.mit.java.paradov.controllers;

import javafx.event.ActionEvent;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import ru.spbau.mit.java.paradov.data.Stats;
import ru.spbau.mit.java.paradov.util.UtilFunctions;

import java.io.IOException;

/**
 * Controller for Stats menu and its layout.
 */
public class StatsMenuController {
    /**
     * Sets up the stage: gets stats from Stats class and writes info in labels.
     * @param stage stage where all labels are
     */
    public static void setStatsScene(Stage stage) {
        ((Label) stage.getScene().lookup("#XWins")).setText(Stats.getXWins().toString());
        ((Label) stage.getScene().lookup("#OWins")).setText(Stats.getOWins().toString());
        ((Label) stage.getScene().lookup("#DrawsPvP")).setText(Stats.getDrawsPvP().toString());

        ((Label) stage.getScene().lookup("#HumanWins")).setText(Stats.getHumanWins().toString());
        ((Label) stage.getScene().lookup("#BotWins")).setText(Stats.getBotWins().toString());
        ((Label) stage.getScene().lookup("#DrawsPvE")).setText(Stats.getDrawsPvE().toString());
    }

    /**
     * Event handler for pressing GoBack button. Creates new scene for main menu layout.
     * @param actionEvent event of pressing GoBack button
     * @throws IOException if there is no main menu layout
     */
    public void returnToMenu(ActionEvent actionEvent) throws IOException {
        Stage stage = UtilFunctions.getStageFromButtonActionEvent(actionEvent);
        UtilFunctions.setStage(getClass(), stage, "/MainMenuLayout.fxml");
    }
}
