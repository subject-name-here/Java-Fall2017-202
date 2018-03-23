package ru.spbau.mit.java.paradov.logic.players;

import ru.spbau.mit.java.paradov.controllers.GameFieldController;
import ru.spbau.mit.java.paradov.logic.Field;
import ru.spbau.mit.java.paradov.logic.GameLogic;

/**
 * Implementation of Player for smart bot. Calculates all possible variants and chooses the best.
 * Works nice only on 3x3 field.
 */
public class HardBot implements Player {
    /**
     * Returns that it is a bot.
     * @return true, because easy bot is a bot
     */
    @Override
    public boolean isBot() {
        return true;
    }

    /**
     * Makes a move by checking if it can win in one move. If it can't, it tries to prevent other
     * player from winning. Then, it tries to occupy some important cell. If all those conditions
     * aren't done, moves to free cell.
     * Works only on 3x3 fields.
     * @param field field where movement will be done
     * @param game game logic, that we need to change turn
     */
    @Override
    public void makeMove(Field field, GameLogic game) {
        boolean wasMove = false;
        int x = 0, y = 0;
        int necX = -1, necY = -1;

        for (int i = 0; i < GameLogic.ROWS; i++) {
            for (int j = 0; j < GameLogic.COLUMNS; j++) {
                if (field.checkCell(i, j)) {
                    x = i;
                    y = j;

                    field.setCell(i, j, game.isXTurn());
                    if (GameLogic.checkIfWin(field)) {
                        field.freeCell(i, j);
                        i = GameLogic.ROWS;
                        wasMove = true;
                        break;
                    }
                    field.setCell(i, j, !game.isXTurn());
                    if (GameLogic.checkIfWin(field)) {
                        necX = i;
                        necY = j;
                    }
                    field.freeCell(i, j);
                }
            }
        }

        if (!wasMove && necX != -1) {
            x = necX;
            y = necY;
            wasMove = true;
        }

        if (!wasMove) {
            if (field.checkCell(1, 1)) {
                x = 1;
                y = 1;
            } else if (field.checkCell(0, 2)) {
                x = 0;
                y = 2;
            } else if (field.checkCell(2, 0)) {
                x = 2;
                y = 0;
            } else if (field.checkCell(2, 2)) {
                x = 2;
                y = 2;
            }
        }

        field.setCell(x, y, game.isXTurn());
        GameFieldController.changeButton(x, y);

        game.changeTurn();
    }

}
