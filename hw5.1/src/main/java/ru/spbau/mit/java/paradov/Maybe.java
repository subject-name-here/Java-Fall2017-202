package ru.spbau.mit.java.paradov;

import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

/**
 * Class that either keeps object of type T, or keeps nothing.
 * @param <T> type of object we keep
 */
public class Maybe<T> {
    /** Flag that tells us if Maybe is empty. */
    private boolean isNothing;

    /**
     * Object, that we keep.
     * If isNothing == true, it probably keeps null.
     */
    private T object;

    private Maybe(boolean isNothing, T object) {
        this.isNothing = isNothing;
        this.object = object;
    }

    /**
     * Constructor that creates not empty Maybe.
     * @param t object that Maybe keeps
     * @param <T> type of object Maybe keeps
     * @return new Maybe object
     */
    @NotNull
    public static <T> Maybe<T> just(T t) {
        return new Maybe<>(false, t);
    }

    /**
     * Constructor that creates empty Maybe.
     * @param <T> type of object Maybe would keep if it would not be empty
     * @return new empty Maybe object
     */
    @NotNull
    public static <T> Maybe<T> nothing() {
        return new Maybe<>(true, null);
    }

    /**
     * Returns object that stored in Maybe or throws an exception.
     * @return object that we keep
     * @throws GetEmptyMaybeException if get() was called on empty Maybe
     */
    public T get() throws GetEmptyMaybeException {
        if (isNothing) {
            throw new GetEmptyMaybeException();
        }

        return object;
    }

    /**
     * Checks if Maybe is not empty.
     * @return true, if Maybe isn't empty, false otherwise
     */
    public boolean isPresent() {
        return !isNothing;
    }

    /**
     * Creates new Maybe using this one, applying function on object we keep,
     * or creates empty Maybe, if this one was empty.
     * @param mapper function to apply on object we keep
     * @param <U> type of value that function returns, thus type of new Maybe
     * @return new Maybe; if this one was empty, it returns empty Maybe;
     * otherwise it returns Maybe with result of mapper applied to object
     */
    public <U> Maybe<U> map(Function<T, U> mapper) {
        if (isNothing) {
            return new Maybe<>(true, null);
        }

        return new Maybe<>(false, mapper.apply(object));
    }

}
