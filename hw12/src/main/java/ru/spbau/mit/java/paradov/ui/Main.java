package ru.spbau.mit.java.paradov.ui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Main method that launches app.
 */
public class Main extends Application {
    /**
     * Initializes app using given stage.
     * @param primaryStage stage where app will be shown
     * @throws IOException if there is no layout for main menu
     */
    @Override
    public void start(Stage primaryStage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("MainMenuLayout.fxml"));
        primaryStage.setTitle("Tic-Tac-Toe");
        primaryStage.setMinHeight(300);
        primaryStage.setMinWidth(300);
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    /**
     * Launches the app using given args. (Actually, I don't know what are those args.)
     * @param args arguments to change app parameters
     */
    public static void main(String[] args) {
        launch(args);
    }
}
