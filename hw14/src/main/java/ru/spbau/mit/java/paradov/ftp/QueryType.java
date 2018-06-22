package ru.spbau.mit.java.paradov.ftp;

/**
 * Enum for types of queries to server.
 */
public enum QueryType {
    /**
     * Constant that stands for command ListFiles.
     */
    LIST_FILES(1),
    /**
     * Constant that stands for command DownloadFile.
     */
    DOWNLOAD_FILE(2);

    private int value;
    private QueryType(int v) {
        value = v;
    }

    /**
     * Returns number corresponding to query type: 1 to ListFiles, 2 to DownloadFile
     * @return number corresponding to query type
     */
    public int getValue() {
        return value;
    }
}
