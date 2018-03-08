package ru.spbau.mit.java.paradov;

/**
 * Exception that LightFuture throws when its supplier has finished with exception.
 */
public class LightExecutionException extends RuntimeException {
    /**
     * Constructs a new exception with the specified cause.
     * @param e the cause of exception
     */
    public LightExecutionException(Exception e) {
        super(e);
    }
}
