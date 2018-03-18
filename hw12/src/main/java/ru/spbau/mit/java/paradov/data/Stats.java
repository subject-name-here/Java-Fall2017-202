package ru.spbau.mit.java.paradov.data;

public class Stats {
    private static Results humanVsHuman = new Results();
    private static Results humanVsBot = new Results();
    private static Results botVsHuman = new Results();

    public static void saveResult (GameType type, GameResult result) {
        Results resultsToUpdate;
        if (type == GameType.PVP) {
            resultsToUpdate = humanVsHuman;
        } else if (type == GameType.BVP) {
            resultsToUpdate = humanVsBot;
        } else {
            resultsToUpdate = botVsHuman;
        }

        if (result == GameResult.X_WON) {
            resultsToUpdate.XWins++;
        } else if (result == GameResult.O_WON) {
            resultsToUpdate.OWins++;
        } else {
            resultsToUpdate.draws++;
        }
    }

    public static Integer getXWins() {
        return humanVsHuman.XWins;
    }

    public static Integer getOWins() {
        return humanVsHuman.OWins;
    }

    public static Integer getDrawsPvP() {
        return humanVsHuman.draws;
    }

    public static Integer getBotWins() {
        return humanVsBot.OWins + botVsHuman.XWins;
    }

    public static Integer getHumanWins() {
        return humanVsBot.XWins + botVsHuman.OWins;
    }

    public static Integer getDrawsPvE() {
        return humanVsBot.draws + botVsHuman.draws;
    }

    private static class Results {
        private int XWins = 0;
        private int OWins = 0;
        private int draws = 0;
    }

}
