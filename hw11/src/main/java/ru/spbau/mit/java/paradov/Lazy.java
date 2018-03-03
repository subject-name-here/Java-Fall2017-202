package ru.spbau.mit.java.paradov;

import org.jetbrains.annotations.Nullable;

/**
 * Interface that wraps some Supplier. Its advantage is that the providing supplier called
 * only at first get(). At next calls, get() returns the same object it returned the first time.
 * @param <T> type of object supplier returns
 */
public interface Lazy<T> {
    /**
     * Gets a result. First time, it launches provided supplier. Next times, it just returns
     * the same object as it returned the first time.
     * @return a result
     */
    @Nullable
    T get();
}
