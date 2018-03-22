package ru.spbau.mit.java.paradov.logic.players;

import ru.spbau.mit.java.paradov.controllers.GameFieldController;
import ru.spbau.mit.java.paradov.logic.Field;
import ru.spbau.mit.java.paradov.logic.GameLogic;

/**
 * Implementation of Player for not smart bot, that puts its symbol in the first free cell.
 */
public class EasyBot implements Player {
    /**
     * Returns that it is a bot.
     * @return true, because easy bot is a bot
     */
    @Override
    public boolean isBot() {
        return true;
    }

    /**
     * Makes a move by searching the first free cell, then putting symbol there.
     * @param field field where movement will be done
     * @param game game logic, that we need to change turn
     */
    @Override
    public void makeMove(Field field, GameLogic game) {
        for (int i = 0; i < GameLogic.ROWS; i++) {
            for (int j = 0; j < GameLogic.COLUMNS; j++) {
                if (field.checkCell(i, j)) {
                    field.setCell(i, j, game.isXTurn());
                    GameFieldController.changeButton(i, j);
                    i = GameLogic.ROWS;
                    break;
                }
            }
        }

        game.changeTurn();
    }
}
