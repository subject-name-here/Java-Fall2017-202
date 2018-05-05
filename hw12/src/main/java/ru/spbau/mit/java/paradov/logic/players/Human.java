package ru.spbau.mit.java.paradov.logic.players;

import ru.spbau.mit.java.paradov.logic.Field;
import ru.spbau.mit.java.paradov.logic.GameLogic;

/**
 * Implementation of Player for human player that presses buttons.
 */
public class Human implements Player {
    /**
     * Returns that it is not a bot.
     * @return false, because human is not a bot
     */
    @Override
    public boolean isBot() {
        return false;
    }

    /**
     * Imitates that it's making move. Actually, it does nothing.
     * @param field field where movement will be done
     * @param game game logic, that we need to change turn
     */
    @Override
    public void makeMove(Field field, GameLogic game) {
        /*
         * There is no implementation, because player makes his moves by pressing button. Something
         * could be here, but now it's unnecessary.
         */
    }
}
