package ru.spbau.mit.java.paradov.controllers;

import javafx.event.ActionEvent;
import javafx.stage.Stage;

import ru.spbau.mit.java.paradov.util.UtilFunctions;

import java.io.IOException;

/**
 * Controller for main menu and its layout.
 */
public class MainController {
    /**
     * Event handler for pressing Exit button. It just closes window.
     * @param actionEvent event of pressing Exit button
     */
    public void closeWindow(ActionEvent actionEvent) {
        UtilFunctions.getStageFromButtonActionEvent(actionEvent).close();
    }

    /**
     * Event handler for pressing Stats button. Creates new scene for stats menu layout.
     * @param actionEvent event of pressing Stats button
     * @throws IOException if there is no stats menu layout
     */
    public void showStats(ActionEvent actionEvent) throws IOException {
        Stage stage = UtilFunctions.getStageFromButtonActionEvent(actionEvent);
        UtilFunctions.setStage(getClass(), stage, "/StatsMenuLayout.fxml");

        StatsMenuController.setStatsScene(stage);  // Init for labels from Stats class
    }

    /**
     * Event handler for pressing Start button. Creates new scene for game settings layout.
     * @param actionEvent event of pressing Start button
     * @throws IOException if there is no game settings layout
     */
    public void startGame(ActionEvent actionEvent) throws Exception {
        Stage stage = UtilFunctions.getStageFromButtonActionEvent(actionEvent);
        UtilFunctions.setStage(getClass(), stage, "/GameSetLayout.fxml");
    }
}
