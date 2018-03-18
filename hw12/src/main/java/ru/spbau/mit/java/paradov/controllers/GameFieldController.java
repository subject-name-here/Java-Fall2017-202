package ru.spbau.mit.java.paradov.controllers;

import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import ru.spbau.mit.java.paradov.logic.GameLogic;
import ru.spbau.mit.java.paradov.util.UtilFunctions;

public class GameFieldController {
    public static void setupField(Stage stage) {
        GridPane metaField = (GridPane) stage.getScene().lookup("#MetaField");
        GridPane field = new GridPane();
        field.setAlignment(Pos.CENTER);

        metaField.add(field, 0, 1);
        for (Integer i = 0; i < GameLogic.ROWS; i++) {
            for (Integer j = 0; j < GameLogic.COLUMNS; j++) {
                Button button = new Button();
                button.setText("");
                button.setId(i.toString() + " " + j.toString());
                button.prefWidthProperty().bind(field.widthProperty().divide(GameLogic.COLUMNS));
                button.prefHeightProperty().bind(field.heightProperty().divide(GameLogic.ROWS));
                button.setOnAction(GameFieldController::buttonPressed);

                field.add(button, j, i);
            }
        }
    }

    public static void buttonPressed(ActionEvent actionEvent) {

    }

    public void goBack(ActionEvent actionEvent) throws Exception {
        Stage stage = UtilFunctions.getStageFromButtonActionEvent(actionEvent);
        UtilFunctions.setStage(getClass(), stage, "GameSetLayout.fxml");
    }
}
