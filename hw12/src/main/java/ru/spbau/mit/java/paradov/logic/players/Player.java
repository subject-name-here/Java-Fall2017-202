package ru.spbau.mit.java.paradov.logic.players;

import ru.spbau.mit.java.paradov.logic.Field;
import ru.spbau.mit.java.paradov.logic.GameLogic;

/**
 * Interface for player in Tic-Tac-Toe. All it can do is answer, if it's bot or not, and make move.
 */
public interface Player {
    /**
     * Answers if this player is a bot. If button pressed by human, but it's bot's turn,
     * pressing will be ignored.
     * @return true, if player is bot; false otherwise
     */
    boolean isBot();

    /**
     * Makes its move on field, if it's bot. If player is human, does nothing, because human makes
     * move not automatically, but by pressing a button.
     * @param field field where movement will be done
     * @param game game logic, that we need to change turn
     */
    void makeMove(Field field, GameLogic game);


    static Player stringToPlayer(String p) {
        String playerType = p.substring(7);
        switch (playerType) {
            case "Human": return new Human();
            case "EasyBot": return new EasyBot();
            case "HardBot": return new HardBot();

            default: return new Human();
        }
    }
}
