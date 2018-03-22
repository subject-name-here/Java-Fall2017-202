package ru.spbau.mit.java.paradov.data;

/**
 * Class for saving game statistics. All data is dropped if window has been closed.
 * Actually, not all data that stored is necessary now (for example, botVsBot doesn't count),
 * but it can be changed by adding getters and changing Stats layout.
 */
public class Stats {
    /** Results of games where human played against human. */
    private static Results humanVsHuman = new Results();
    /** Results of games where first player was human, second player was bot. */
    private static Results humanVsBot = new Results();
    /** Results of games where second player was human, first player was bot. */
    private static Results botVsHuman = new Results();
    /** Results of games where bot played against bot. */
    private static Results botVsBot = new Results();

    /**
     * Saves result of the game.
     * @param type type of the game
     * @param result result of the game
     */
    public static void saveResult (GameType type, GameResult result) {
        Results resultsToUpdate;

        if (type == GameType.PVP) {
            resultsToUpdate = humanVsHuman;
        } else if (type == GameType.PVB) {
            resultsToUpdate = humanVsBot;
        } else if (type == GameType.BVP) {
            resultsToUpdate = botVsHuman;
        } else {
            resultsToUpdate = botVsBot;
        }

        if (result == GameResult.X_WON) {
            resultsToUpdate.XWins++;
        } else if (result == GameResult.O_WON) {
            resultsToUpdate.OWins++;
        } else {
            resultsToUpdate.draws++;
        }
    }

    /**
     * Returns how many times 1st player won in PvP.
     * @return how many times 1st player won in PvP
     */
    public static Integer getXWins() {
        return humanVsHuman.XWins;
    }

    /**
     * Returns how many times 2nd player won in PvP.
     * @return how many times 2nd player won in PvP
     */
    public static Integer getOWins() {
        return humanVsHuman.OWins;
    }

    /**
     * Returns how many times there was a draw in PvP.
     * @return how many times there was a draw in PvP
     */
    public static Integer getDrawsPvP() {
        return humanVsHuman.draws;
    }

    /**
     * Returns how many times bot defeated human.
     * @return how many times bot defeated human
     */
    public static Integer getBotWins() {
        return humanVsBot.OWins + botVsHuman.XWins;
    }

    /**
     * Returns how many times bot was defeated by human.
     * @return how many times bot was defeated by human
     */
    public static Integer getHumanWins() {
        return humanVsBot.XWins + botVsHuman.OWins;
    }

    /**
     * Returns how many times there was a draw between human and bot.
     * @return how many times there was a draw between human and bot
     */
    public static Integer getDrawsPvE() {
        return humanVsBot.draws + botVsHuman.draws;
    }

    /**
     * Class that contains all possible result of game.
     */
    private static class Results {
        /** Counter of wins of 1st player. */
        private int XWins = 0;
        /** Counter of wins of 2nd player. */
        private int OWins = 0;
        /** Counter of draws. */
        private int draws = 0;
    }

}
