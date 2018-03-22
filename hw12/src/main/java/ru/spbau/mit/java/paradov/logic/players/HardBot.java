package ru.spbau.mit.java.paradov.logic.players;

import ru.spbau.mit.java.paradov.controllers.GameFieldController;
import ru.spbau.mit.java.paradov.logic.Field;
import ru.spbau.mit.java.paradov.logic.GameLogic;

/**
 * Implementation of Player for smart bot. Calculates all possible variants and chooses the best.
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
     * Makes a move by calculating everything,
     * @param field field where movement will be done
     * @param game game logic, that we need to change turn
     */
    @Override
    public void makeMove(Field field, GameLogic game) {
        

        game.changeTurn();
    }

    private boolean dfs
}
