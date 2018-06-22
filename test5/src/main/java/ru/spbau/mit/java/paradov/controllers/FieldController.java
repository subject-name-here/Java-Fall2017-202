package ru.spbau.mit.java.paradov.controllers;

import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import ru.spbau.mit.java.paradov.ui.Main;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Controller for field of game. It controls stage setup (creates field full of buttons)
 * and button pressings.
 */
public class FieldController {
    /** Stage where this layout placed. Needed to change some labels after endgame. */
    private static Stage currentStage;

    /** Values hidden under the buttons. */
    private static int[][] values;

    /** Counts how many buttons are disabled. */
    private static int disabledButtons;

    /** Saves the pressed button when there is no second button to compare values. */
    private static Button pressed;

    /**
     * Initializes class fields and setups the stage: puts many buttons of proper size.
     * @param stage stage where buttons will be placed
     */
    public static void setupGame(Stage stage) {
        int fieldSize = Main.getFieldSize();

        currentStage = stage;

        GridPane metaField = (GridPane) stage.getScene().lookup("#MetaField");
        GridPane field = new GridPane();
        field.setAlignment(Pos.CENTER);

        metaField.add(field, 0, 1);
        for (Integer i = 0; i < fieldSize; i++) {
            for (Integer j = 0; j < fieldSize; j++) {
                Button button = new Button();
                button.setText(" ");
                button.setId(i.toString() + " " + j.toString());
                button.prefWidthProperty().bind(field.widthProperty().divide(fieldSize));
                button.prefHeightProperty().bind(field.heightProperty().divide(fieldSize));
                button.setOnAction(FieldController::buttonPressed);

                // Coordinates are in wrong order, because it's said so in signature of method.
                field.add(button, j, i);
            }
        }


        values = setValues(fieldSize);
        disabledButtons = 0;

        pressed = null;
    }

    /**
     * Process the button pressing: if the button pressed without pair, it just shows its value.
     * If the button already has pair, depending on values they are either become disabled
     * or wait until return to its initial states.
     * @param actionEvent event of pressing on some of field button
     */
    public static void buttonPressed(ActionEvent actionEvent) {
        Button current = (Button) actionEvent.getSource();
        if (current == pressed) {
            return;
        }

        current.setText(String.valueOf(getValueByButton(current)));

        if (pressed == null) {
            pressed = current;
            return;
        }


        if (getValueByButton(pressed) == getValueByButton(current)) {
            current.setDisable(true);
            pressed.setDisable(true);

            disabledButtons += 2;

            if (disabledButtons == Main.getFieldSize() * Main.getFieldSize()) {
                processEnd();
            }

        } else {
            waitThenClean(pressed, current);
        }

        pressed = null;
    }

    /**
     * Processing the endgame: changes title text and button text.
     */
    private static void processEnd() {
        Label title = (Label) currentStage.getScene().lookup("#State");
        title.setText("You won!");

        Button exitButton = (Button) currentStage.getScene().lookup("#GoBack");
        exitButton.setText("Exit.");
    }

    /**
     * Gets value hidden under given button.
     * @param button button, value of which we want to know
     * @return value hidden under given button
     */
    private static int getValueByButton(Button button) {
        String[] coordinates = button.getId().split(" ");
        Integer row = Integer.decode(coordinates[0]);
        Integer col = Integer.decode(coordinates[1]);
        return values[row][col];
    }

    /**
     * Returns field filled with random numbers from 0 to n^2 / 2, so that every number
     * is placed on the field exactly twice.
     * @param n dimension of field
     * @return filled field
     */
    private static int[][] setValues(int n) {
        int[][] field = new int[n][n];

        ArrayList<Integer> rand = new ArrayList<>();
        for (int i = 0; i < n * n / 2; i++) {
            rand.add(i);
            rand.add(i);
        }

        Collections.shuffle(rand);
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                field[i][j] = rand.get(i * n + j);
            }
        }

        return field;
    }

    /**
     * Processes the pressing on "Exit" button: closes the window.
     * @param actionEvent event of pressing on "Exit" button
     */
    public void exit(ActionEvent actionEvent) {
        Button button = (Button) actionEvent.getSource();
        Stage stage = (Stage) button.getScene().getWindow();
        stage.close();
    }

    /**
     * A very specific util function: waits a second, then sets text on buttons to empty.
     * @param b1 button 1
     * @param b2 button 2
     */
    private static void waitThenClean(Button b1, Button b2) {
        Task<Void> sleeper = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {}

                return null;
            }
        };

        sleeper.setOnSucceeded(event -> {
            b1.setText(" ");
            b2.setText(" ");
        });

        new Thread(sleeper).start();
    }
}
