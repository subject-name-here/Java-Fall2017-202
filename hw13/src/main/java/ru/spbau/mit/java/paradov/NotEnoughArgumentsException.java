package ru.spbau.mit.java.paradov;

/**
 * Unchecked exception thrown if user had not provided necessary number of arguments
 */
public class NotEnoughArgumentsException extends RuntimeException {
    /**
     * Constructor that just calls constructor of super class.
     * @param s
     */
    public NotEnoughArgumentsException(String s) {
        super(s);
    }
}
