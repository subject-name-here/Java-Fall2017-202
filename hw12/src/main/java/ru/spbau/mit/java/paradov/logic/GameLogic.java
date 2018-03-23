package ru.spbau.mit.java.paradov.logic;

import ru.spbau.mit.java.paradov.controllers.GameFieldController;
import ru.spbau.mit.java.paradov.data.GameResult;
import ru.spbau.mit.java.paradov.data.GameType;
import ru.spbau.mit.java.paradov.logic.players.Player;

/**
 * Game master, that controls the game's logic. It doesn't have to do something with the layout;
 * it only controls game, rules, field and turns.
 */
public class GameLogic {
    /** Number of rows on the field. */
    public static final int ROWS = 3;
    /** Number of columns on the field. */
    public static final int COLUMNS = 3;

    /** Number of symbols that players need to put in line to win. */
    private static final int LINE = 3;
    /** Coordinates change on winning line. */
    private static final int[] DIRECTIONS = {-1, 0, 1};

    /** Player 1, whose symbols are X. */
    private Player player1;
    /** Player 2, whose symbols are O. */
    private Player player2;

    /** Field where all game is written. */
    private Field field;
    /** Flag detecting if it's 1st player's turn. */
    private boolean isXTurn;
    /** Flag detecting if game is over. */
    private boolean gameOver;

    /**
     * Creates a new game.
     * @param p1 identifier of player 1
     * @param p2 identifier of player 2
     */
    public GameLogic(String p1, String p2) {
        player1 = Player.stringToPlayer(p1);
        player2 = Player.stringToPlayer(p2);

        field = new Field(ROWS, COLUMNS);
        isXTurn = true;
        gameOver = false;
    }

    /**
     * Begins the game, letting player 1 make his move.
     */
    public void beginGame() {
        player1.makeMove(field, this);
    }

    /**
     * Changes turn between players after every move. It checks every time,
     * if last move was winning or it was last in the game, and if one of it is true,
     * handles it properly.
     */
    public void changeTurn() {
        if (checkIfWin(field)) {
            gameOver = true;
            GameFieldController.finishGame(isXTurn ? GameResult.X_WON : GameResult.O_WON);
            return;
        }

        if (field.isFull()) {
            gameOver = true;
            GameFieldController.finishGame(GameResult.DRAW);
            return;
        }

        isXTurn = !isXTurn;
        GameFieldController.changeLabelTurn();

        if (isXTurn) {
            player1.makeMove(field, this);
        } else {
            player2.makeMove(field, this);
        }

    }

    /**
     * Checks if someone has won on the field.
     * @return true, if there is winning line on field, false otherwise
     */
    public static boolean checkIfWin(Field field) {
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLUMNS; j++) {
                char curr = field.getCell(i, j);
                if (curr == ' ')
                    continue;

                for (int x : DIRECTIONS) {
                    for (int y : DIRECTIONS) {
                        if (x != 0 || y != 0) {
                            boolean flag = true;
                            for (int k = 1; k < LINE; k++) {
                                if (field.getCell(i + k * x, j + k * y) != curr) {
                                    flag = false;
                                    break;
                                }
                            }
                            if (flag) {
                                return true;
                            }
                        }
                    }
                }

            }
        }

        return false;
    }

    /**
     * Handles the button press: checks if it is human's turn, if game isn't over, is move valid,
     * and only then it makes move as current human-player.
     * @param r line where move is done
     * @param c column where move is done
     */
    public void buttonPressed(Integer r, Integer c) {
        if (isXTurn && player1.isBot()
                || !isXTurn && player2.isBot()
                || gameOver
                || !field.checkCell(r, c)) {
            return;
        }

        field.setCell(r, c, isXTurn);
        GameFieldController.changeButton(r, c);

        changeTurn();
    }

    /**
     * Returns if it's 1st player turn or not.
     * @return true if it's 1st player turn, false otherwise
     */
    public boolean isXTurn() {
        return isXTurn;
    }

    /**
     * Returns type of this game. Used to save result in game stats.
     * @return type of this game
     */
    public GameType getGameType() {
        if (player1.isBot()) {
            if (player2.isBot()) {
                return GameType.BVB;
            } else {
                return GameType.BVP;
            }
        } else {
            if (player2.isBot()) {
                return GameType.PVB;
            } else {
                return GameType.PVP;
            }
        }
    }
}
