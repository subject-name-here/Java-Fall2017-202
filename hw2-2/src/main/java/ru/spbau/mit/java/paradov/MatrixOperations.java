package ru.spbau.mit.java.paradov;

import java.util.Comparator;

import static java.util.Arrays.sort;

/**
 * Class for operations with matrices.
 */
public class MatrixOperations {

    /**
     * Method walks in matrix in spiral from center (up - left - down - right),
     * writing elements in StringBuilder, which will be converted to String.
     *
     * @param matrix matrix to write in spiral order.
     * @return string with mtrix elements written in spiral order.
     */
    public static String spiralOutput(final int[][] matrix){
        StringBuilder buf = new StringBuilder();

        int n = matrix.length;
        int center = (n - 1) / 2;

        buf.append(matrix[center][center]);
        buf.append(" ");

        for (int l = 1; l < (n + 1) / 2; l++) {
            for (int i = 0; i < 2 * l; i++) {
                buf.append(matrix[center - l][center - l + 1 + i]);
                buf.append(" ");
            }

            for (int i = 0; i < 2 * l; i++) {
                buf.append(matrix[center - l + 1 + i][center + l]);
                buf.append(" ");
            }

            for (int i = 0; i < 2 * l; i++) {
                buf.append(matrix[center + l][center + l - 1 - i]);
                buf.append(" ");
            }

            for (int i = 0; i < 2 * l; i++) {
                buf.append(matrix[center + l - 1 - i][center - l]);
                buf.append(" ");
            }
        }

        return buf.toString();
    }

    /**
     * Method sorts matrix by first element of its column.
     * @param matrix - matrix to be sorted.
     */
    public static void sortColumns(int[][] matrix){
        transposeMatrix(matrix);
        sort(matrix, Comparator.comparingInt(o -> o[0]));
        transposeMatrix(matrix);
    }

    /**
     * Function for transposing matrix. Used in sortColumns.
     *
     * @param matrix - matrix which we must transpose
     */
    private static void transposeMatrix(int[][] matrix){
        int tmp;

        for (int i = 0; i < matrix.length; i++){
            for (int j = i + 1; j < matrix.length; j++){
                tmp = matrix[i][j];
                matrix[i][j] = matrix[j][i];
                matrix[j][i] = tmp;
            }
        }
    }
}
