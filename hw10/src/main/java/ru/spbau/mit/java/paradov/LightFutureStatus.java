package ru.spbau.mit.java.paradov;

/**
 * Variants of status of LightFuture's supplier computation.
 */
public enum LightFutureStatus {
    NOT_READY,
    READY,
    FINISHED_WITH_EXCEPTION
}
