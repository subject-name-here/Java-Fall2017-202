package ru.spbau.mit.java.paradov.controllers;

import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import ru.spbau.mit.java.paradov.util.UtilFunctions;

import java.io.IOException;

public class SettingsController {
    public void goToClient(ActionEvent actionEvent) throws IOException {
        Stage stage = UtilFunctions.getStageFromButtonActionEvent(actionEvent);
        Scene scene = stage.getScene();

        String hostname = ((TextField) scene.lookup("#HostName")).getText();
        String portNum = ((TextField) scene.lookup("#PortNumber")).getText();
        String delimiter = ((TextField) scene.lookup("#Delimiter")).getText();
        String root = ((TextField) scene.lookup("#Root")).getText();
        MainController.setupMain(hostname, portNum, delimiter, root, stage);

        UtilFunctions.setStage(getClass(), stage, "/MainLayout.fxml");
    }
}
