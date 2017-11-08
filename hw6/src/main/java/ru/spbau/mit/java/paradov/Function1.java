package ru.spbau.mit.java.paradov;

import org.jetbrains.annotations.NotNull;

/**
 * Interface that represents function of one argument.
 * @param <T> type of argument
 * @param <R> type function's result
 */
public interface Function1 <T, R> {
    /**
     * Applies this function to the given argument.
     * @param arg the function argument
     * @return the function result
     */
    public R apply(T arg);

    /**
     * Makes a composition of given function and this one.
     * Apply of a result function is apply of this function, and then apply of given function
     * to a result of first apply.
     * @param g given function of one argument
     * @param <R2> type of result of given function, therefore result of function we return
     * @return function of one argument - composition of this and given one
     */
    default public <R2> @NotNull Function1<T, R2> compose(@NotNull Function1<R, R2> g) {
        return new Function1<T, R2>() {
            @Override
            public R2 apply(T arg) {
                return g.apply(Function1.this.apply(arg));
            }
        };
    }
}
