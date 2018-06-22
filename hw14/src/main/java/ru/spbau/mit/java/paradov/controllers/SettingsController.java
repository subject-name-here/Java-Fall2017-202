package ru.spbau.mit.java.paradov.controllers;

import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import ru.spbau.mit.java.paradov.util.UtilFunctions;

import java.io.IOException;

/**
 * Controller for stage with settings. Does only one thing: proceed to main screen with data.
 */
public class SettingsController {
    /**
     * Go to main screen with entered in text fields data.
     * @param actionEvent event of pressing on button "Continue"
     * @throws IOException if there is no layout for main screen
     */
    public void goToClient(ActionEvent actionEvent) throws IOException {
        Stage stage = UtilFunctions.getStageFromButtonActionEvent(actionEvent);
        Scene scene = stage.getScene();

        String hostname = ((TextField) scene.lookup("#HostName")).getText();
        String portNum = ((TextField) scene.lookup("#PortNumber")).getText();
        String delimiter = ((TextField) scene.lookup("#Delimiter")).getText();
        String root = ((TextField) scene.lookup("#Root")).getText();
        MainController.setupMain(hostname, portNum, delimiter, root);

        UtilFunctions.setStage(getClass(), stage, "/MainLayout.fxml");
    }
}
