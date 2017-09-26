package ru.spbau.mit.java.paradov;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Class for testing methods in MatrixOperations class.
 */
public class MatrixOperationsTest {
    @Test
    /**
     * Tests if SpiralOutput works fine on 1x1 matrices.
     */
    public void testSpiralOutputOneOnOne() {
        int[][] matrix = new int[1][1];
        matrix[0][0] = 5;

        String s = MatrixOperations.spiralOutput(matrix);
        assertEquals('5', s.charAt(0));
    }

    @Test
    /**
     * Tests if SpiralOutput works correct on matrix 5x5.
     */
    public void testSpiralOutput() {
        int[][] matrix = new int[5][5];

        for (int i = 0; i < 5; i++){
            for (int j = 0; j < 5; j++){
                matrix[i][j] = i * 5 + j;
            }
        }

        String result = MatrixOperations.spiralOutput(matrix);

        String expected =
                "12 7 8 13 18 17 16 11 6 " +
                        "1 2 3 4 9 14 19 24 23 22 21 20 15 10 5 0 ";

        MatrixOperations.spiralOutput(matrix);
        assertEquals(expected, result);
    }

    @Test
    /**
     * Tests if SortColumns works on 1x1 matrices.
     */
    public void testSortColumnsOneOnOne() {
        int[][] matrix = new int[1][1];
        matrix[0][0] = 42;

        MatrixOperations.sortColumns(matrix);

        assertEquals(42, matrix[0][0]);
    }

    @Test
    /**
     * Tests if SortColumns works on 5x5 matrices.
     */
    public void testSortColumns() {
        int[][] matrix = new int[5][5];

        for (int i = 0; i < 5; i++){
            for (int j = 0; j < 5; j++){
                matrix[i][j] = i * 5 + 4 - j;
            }
        }

        MatrixOperations.sortColumns(matrix);

        assertEquals(1, matrix[0][1]);
    }

}