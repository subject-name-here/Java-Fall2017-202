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
    /** Size of field dimension, given in command line argument. */
    private static int fieldSize;

    public static int getFieldSize() {
        return fieldSize;
    }

    /**
     * Initializes app using given stage.
     * @param primaryStage stage where app will be shown
     * @throws IOException if there is no layout for main menu
     */
    @Override
    public void start(Stage primaryStage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/MainMenuLayout.fxml"));
        primaryStage.setTitle("Pairs");
        primaryStage.setMinHeight(600);
        primaryStage.setMinWidth(600);
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    /**
     * Launches the app using given args. Also, it checks args on correctness: first arg must
     * be even natural integer less or equal than 12.
     * @param args arguments to change app parameters
     */
    public static void main(String[] args) {
        if (args.length == 0) {
            System.err.println("Required argument: N - size of field. (N must be even!)");
            System.exit(1);
        }

        fieldSize = Integer.parseInt(args[0]);
        if (fieldSize % 2 == 1) {
            System.err.println("Cannot create game with odd field size!");
            System.exit(1);
        }

        if (fieldSize <= 0) {
            System.err.println("Field size is wrong: it's 0 or less...");
            System.exit(1);
        }

        if (fieldSize > 12) {
            System.err.println("Field size is too big!");
            System.exit(1);
        }

        launch(args);
    }
}

