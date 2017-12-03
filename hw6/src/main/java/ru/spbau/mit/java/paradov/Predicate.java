package ru.spbau.mit.java.paradov;

import org.jetbrains.annotations.NotNull;

/**
 * Interface that represents predicate - function of one argument that returns true or false.
 * @param <T> type of function's argument
 */
public interface Predicate <T> extends Function1<T, Boolean> {
    /**
     * Returns constant predicate; its apply() always returns true.
     * Type of argument is Object, because it doesn't matter what argument apply() will get.
     * @return predicate with apply() that always returns true
     */
    default public @NotNull Predicate<?> ALWAYS_TRUE() {
        return new Predicate<Object>() {
            @Override
            public Boolean apply(Object arg) {
                return true;
            }
        };
    }

    /**
     * Returns constant predicate; its apply() always returns false.
     * Type of argument is Object, because it doesn't matter what argument apply() will get.
     * @return predicate with apply() that always returns false
     */
    default public @NotNull Predicate<?> ALWAYS_FALSE() {
        return new Predicate<Object>() {
            @Override
            public Boolean apply(Object arg) {
                return false;
            }
        };
    }

    /**
     * Creates predicate; its apply() is logical "or" of this predicate's apply() and given one's.
     * If apply() of this function have returned true, apply() of given function is not called
     * and true is returned ("or" is lazy).
     * @param g predicate we are making "or" with
     * @return predicate with apply() acting like a lazy logical "or" of apply() of this function
     * and given one
     */
    default public @NotNull Predicate<T> or(@NotNull Predicate<T> g) {
        return new Predicate<T>() {
            @Override
            public @NotNull Boolean apply(T arg) {
                return Predicate.this.apply(arg) || g.apply(arg);
            }
        };
    }

    /**
     * Creates predicate; its apply() is logical "and" of this predicate's apply() and given one's.
     * If apply() of this function have returned false, apply() of given function is not called
     * and false is returned ("and" is lazy).
     * @param g predicate we are making "and" with
     * @return predicate with apply() acting like a lazy logical "and" of apply() of this function
     * and given one
     */
    default public @NotNull Predicate<T> and(@NotNull Predicate<T> g) {
        return new Predicate<T>() {
            @Override
            public @NotNull Boolean apply(T arg) {
                return Predicate.this.apply(arg) && g.apply(arg);
            }
        };
    }

    /**
     * Creates predicate; its apply() is logical "not" used on this function's apply() result.
     * @return predicate with apply() as in this function, but its result is negated
     */
    default public @NotNull Predicate<T> not() {
        return new Predicate<T>() {
            @Override
            public Boolean apply(T arg) {
                return !Predicate.this.apply(arg);
            }
        };
    }
}
