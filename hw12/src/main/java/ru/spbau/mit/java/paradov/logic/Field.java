package ru.spbau.mit.java.paradov.logic;

/**
 * A class for Tic-Tac-Toe's field. It is just a wrapper for 2d char array, and it doesn't check
 * if movement is valid, and all that things (it's up to GameLogic).
 */
public class Field {
    /** Field for empty spaces, X and O. */
    private char[][] field;

    /** Number of rows in this field. */
    private int rows;
    /** Number of columns in this field. */
    private int columns;

    /**
     * Creates new field with given parameters.
     * @param r number of rows in field
     * @param c number of columns in field
     */
    public Field(int r, int c) {
        rows = r;
        columns = c;
        field = new char[r][c];

        for (int i = 0; i < r; i++) {
            for (int j = 0; j < c; j++) {
                field[i][j] = ' ';
            }
        }
    }

    /**
     * Checks if given cell is empty.
     * @param r number of cell's row
     * @param c number of cell's column
     * @return true, if cell is empty; false otherwise
     */
    public boolean checkCell(int r, int c) {
        return field[r][c] == ' ';
    }

    /**
     * Returns given cell.
     * @param r number of cell's row
     * @param c number of cell's column
     * @return cell with given coordinates.
     */
    public char getCell(int r, int c) {
        if (r < 0 || r >= rows || c < 0 || c >= columns)
            return '\0';

        return field[r][c];
    }

    /**
     * Sets given cell as someone's cell.
     * @param r number of cell's row
     * @param c number of cell's column
     * @param isXTurn determines who has the given cell: first player has it iff this flag is true
     */
    public void setCell(int r, int c, boolean isXTurn) {
        field[r][c] = isXTurn ? 'X' : 'O';
    }

    /**
     * Frees given cell from mark.
     * @param r row of the given cell
     * @param c column of the given cell
     */
    public void freeCell(int r, int c) {
        field[r][c] = ' ';
    }

    /**
     * Checks if field is full, so no marks can be installed.
     * @return true, if field is full, false otherwise
     */
    public boolean isFull() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                if (field[i][j] == ' ') {
                    return false;
                }
            }
        }

        return true;
    }
}
