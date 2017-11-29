package ru.spbau.mit.java.paradov;

/**
 * It is thrown if incorrect expression was given to us. It could be thrown when parsing given string,
 * because it has some wrong symbols, or trying to calculate the expression in RPN.
 */
public class IncorrectExpressionException extends Exception {
}
