package ru.spbau.mit.java.paradov;

import org.junit.Test;

import static org.junit.Assert.*;

/** Tests class ExpressionParses. More precisely, it tests its one method of cast string expression to stack expression-in-RPN. */
public class ExpressionParserTest {
    /** Tests if empty expression casts correctly without exceptions. */
    @Test
    public void testCastOfEmptyExpression() throws Exception {
        assertTrue(ExpressionParser.expressionToRPNStack("").isEmpty());
    }

    /** Tests if cast of small expression works correctly and does not throws exception. */
    @Test
    public void testCastOfSmallExpression() throws Exception {
        String expression = "13 + 5";
        MyStack<String> stack = ExpressionParser.expressionToRPNStack(expression);

        String[] actual = new String[3];
        for (int i = 0; i < 3; i++)
            actual[2 - i] = stack.pop();

        String[] expected = {"13", "5", "+"};
        assertArrayEquals(expected, actual);
    }

    /** Tests if cast of expression with different operartions works correctly
     * and does not throws exception.
     */
    @Test
    public void testCastOfBigExpression() throws Exception {
        String expression = "13 - 5*3 + 39";
        MyStack<String> stack = ExpressionParser.expressionToRPNStack(expression);

        int expectedSize = 7;
        String[] actual = new String[expectedSize];
        for (int i = 0; i < expectedSize; i++)
            actual[expectedSize - i - 1] = stack.pop();

        String[] expected = {"13", "5", "3", "*", "-", "39", "+"};
        assertArrayEquals(expected, actual);
    }

    /**
     * Tests if cast of expression with different operartions and braces works correctly
     * and does not throws exception.
     */
    @Test
    public void testCastOfBracedExpression() throws Exception {
        String expression = "(13 - 5)*3 + 2";
        MyStack<String> stack = ExpressionParser.expressionToRPNStack(expression);

        int expectedSize = 7;
        String[] actual = new String[expectedSize];
        for (int i = 0; i < expectedSize; i++)
            actual[expectedSize - i - 1] = stack.pop();

        String[] expected = {"13", "5", "-", "3", "*", "2", "+"};
        assertArrayEquals(expected, actual);
    }

    /**
     * Tests if cast of expression big and complicated expression works correctly
     * and does not throws exception.
     */
    @Test
    public void testCastOfMixedExpression() throws Exception {
        String expression = "(13 - 5)*3 + 2* 3 -(11 + 5)";
        MyStack<String> stack = ExpressionParser.expressionToRPNStack(expression);

        int expectedSize = 13;
        String[] actual = new String[expectedSize];
        for (int i = 0; i < expectedSize; i++)
            actual[expectedSize - i - 1] = stack.pop();

        String[] expected = {"13", "5", "-", "3", "*", "2", "3", "*", "+", "11", "5", "+", "-"};
        assertArrayEquals(expected, actual);
    }

    /**
     * Tests if cast of expression with bad bracing throws exception.
     */
    @Test(expected = IncorrectExpressionException.class)
    public void testCastOfThrowingExceptionsWhenBraceOpened() throws Exception {
        String expression = "13 - (5*2";
        MyStack<String> stack = ExpressionParser.expressionToRPNStack(expression);
    }

    /**
     * Tests if cast of expression with wrong arguments throws exception.
     */
    @Test(expected = IncorrectExpressionException.class)
    public void testCastOfThrowingExceptionsWhenExpressionIsWrong() throws Exception {
        String expression = "HELP I'M TRAPPED IN TESTS";
        MyStack<String> stack = ExpressionParser.expressionToRPNStack(expression);
    }
}