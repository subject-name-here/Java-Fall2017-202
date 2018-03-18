package ru.spbau.mit.java.paradov.util;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class UtilFunctions {
    public static Stage getStageFromButtonActionEvent(ActionEvent actionEvent) {
        Button button = (Button) actionEvent.getSource();
        return (Stage) button.getScene().getWindow();
    }

    public static void setStage(Class<?> controller, Stage stage, String layout) throws Exception {
        Parent root = FXMLLoader.load(controller.getClassLoader().getResource(layout));
        stage.setScene(new Scene(root, stage.getScene().getWidth(), stage.getScene().getHeight()));
    }
}
