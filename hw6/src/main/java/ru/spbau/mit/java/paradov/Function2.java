package ru.spbau.mit.java.paradov;

import org.jetbrains.annotations.NotNull;

/**
 * Interface that represents function of two arguments.
 * @param <T1> type of a first argument
 * @param <T2> type of a second argument
 * @param <R> type of a result
 */
public interface Function2 <T1, T2, R> {
    /**
     * Applies this function to given arguments.
     * @param arg1 first argument
     * @param arg2 second argument
     * @return result of a function applied to given arguments
     */
    public R apply(T1 arg1, T2 arg2);

    /**
     * Makes a composition of a given function and this function.
     * Apply of a result function is apply of this function, and then apply of given function
     * to a result of first apply.
     * @param g given function of one argument
     * @param <R2> type of result of given function, therefore result of function we return
     * @return function of two arguments - composition of this function and given one
     */
    default public <R2> @NotNull Function2<T1, T2, R2> compose(@NotNull Function1<R, R2> g) {
        return new Function2<T1, T2, R2>() {
            @Override
            public R2 apply(T1 arg1, T2 arg2) {
                return g.apply(Function2.this.apply(arg1, arg2));
            }
        };
    }

    /**
     * Binds first argument of this function. Result is a function of two arguments
     * with the same apply() as here, but first argument is ignored
     * (even its type doesn't matter, now type of a first argument is <code>Object</code>).
     * Instead of it, as a first argument in apply() we use parameter that we have got here.
     * @param argToBind argument that will be used as first argument of a result function
     * @return function of a two arguments, where type of a first argument is <code>Object</code>,
     * apply() is the same as in this function, but it uses given parameter as a first argument
     */
    default public @NotNull Function2<Object, T2, R> bind1(T1 argToBind) {
        return new Function2<Object, T2, R>() {
            @Override
            public R apply(Object arg1, T2 arg2) {
                return Function2.this.apply(argToBind, arg2);
            }
        };
    }

    /**
     * Binds second argument of this function. Result is a function of two arguments
     * with the same apply() as here, but second argument is ignored
     * (even its type doesn't matter, now type of a second argument is <code>Object</code>).
     * Instead of it, as a second argument in apply() we use parameter that we have got here.
     * @param argToBind argument that will be used as second argument of a result function
     * @return function of a two arguments, where type of a second argument is <code>Object</code>,
     * apply() is the same as in this function, but it uses given parameter as a second argument
     */
    default public @NotNull Function2<T1, Object, R> bind2(T2 argToBind) {
        return new Function2<T1, Object, R>() {
            @Override
            public R apply(T1 arg1, Object arg2) {
                return Function2.this.apply(arg1, argToBind);
            }
        };
    }

    /**
     * Reduces number of arguments of the function. This function turns to a function
     * with one argument: first argument now fixed (its value is parameter that we have got),
     * and the second argument is now argument of a result function.
     * @param argToCurry argument that will replace first argument
     * @return function of one argument; its apply() is the same as apply() of this function,
     * but first argument is parameter we have got and second argument is free
     */
    default public @NotNull Function1<T2, R> curry(T1 argToCurry) {
        return new Function1<T2, R>() {
            @Override
            public R apply(T2 arg2) {
                return Function2.this.apply(argToCurry, arg2);
            }
        };
    }
}
