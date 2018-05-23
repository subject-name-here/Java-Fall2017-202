package ru.spbau.mit.java.paradov.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Controller for main menu screen. Controls only press on two buttons.
 */
public class MainController {
    /**
     * Processes pressing on button "New game": sets new scene and calls a game setup.
     * @param actionEvent event of pressing on "New game" button
     * @throws IOException if there is no FieldLayout.fxml
     */
    public void startGame(ActionEvent actionEvent) throws IOException {
        Button button = (Button) actionEvent.getSource();
        Stage stage = (Stage) button.getScene().getWindow();

        Parent root = FXMLLoader.load(this.getClass().getResource("/FieldLayout.fxml"));
        stage.setScene(new Scene(root, stage.getScene().getWidth(), stage.getScene().getHeight()));

        FieldController.setupGame(stage);
    }

    /**
     * Processes pressing on button "Exit": closes the stage, therefore, app is stopped.
     * @param actionEvent event of pressing on "Exit" button
     */
    public void closeWindow(ActionEvent actionEvent) {
        Button button = (Button) actionEvent.getSource();
        Stage stage = (Stage) button.getScene().getWindow();
        stage.close();
    }
}
