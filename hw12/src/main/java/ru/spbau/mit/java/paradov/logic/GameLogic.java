package ru.spbau.mit.java.paradov.logic;

import javafx.stage.Stage;

public class GameLogic {
    public static final int ROWS = 3;
    public static final int COLUMNS = 3;
    public static final int LINE = 3;

    private static Stage fieldStage;
    private static String player1;
    private static String player2;

    private static boolean isXTurn;

    public static void setPlayers() {

    }

    public static void beginGame(Stage stage, String p1, String p2) {
        fieldStage = stage;
        player1 = p1;
        player2 = p2;
        isXTurn = true;

        if (player1.equals("Player1EasyBot")) {

        } else if (player1.equals("Player1HardBot")) {

        }
    }

    public static void changeTurn() {
        fieldStage.getScene().lookup("#Turn");
    }
}
