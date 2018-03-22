package ru.spbau.mit.java.paradov.util;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Some utility functions to deal with stage system. Those are used in some controllers.
 */
public class UtilFunctions {
    /**
     * Gets button from given ActionEvent.
     * @param actionEvent event of pressing button
     * @return button
     */
    public static Stage getStageFromButtonActionEvent(ActionEvent actionEvent) {
        Button button = (Button) actionEvent.getSource();
        return (Stage) button.getScene().getWindow();
    }

    /**
     * Sets stage with given layout.
     * @param controller controller of given layout
     * @param stage stage where we want to put layout
     * @param layout layout we want to put on stage
     * @throws IOException if there is no such layout
     */
    public static void setStage(Class<?> controller, Stage stage, String layout) throws IOException {
        Parent root = FXMLLoader.load(controller.getClassLoader().getResource(layout));
        stage.setScene(new Scene(root, stage.getScene().getWidth(), stage.getScene().getHeight()));
    }

}
