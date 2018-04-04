package ru.spbau.mit.java.paradov;

import java.util.function.Function;

/**
 * Interface for tasks that were added in ThreadPoolImpl.
 * @param <T> type of returning value
 */
public interface LightFuture<T> {
    /**
     * Checks if computation of task is over, and result can be returned.
     * @return true if result is ready, false otherwise
     */
    public boolean isReady();

    /**
     * Returns result of computation, when it's done.
     * @return result of computation
     * @throws LightExecutionException if providing supplier has finished work with exception
     */
    public T get() throws LightExecutionException;

    /**
     * Takes Function object, then applies it to result of this LightFuture and returns new
     * LightFuture object as a task accepted in ThreadPool.
     * @param function function to apply to this function's result
     * @return new LightFuture object as a task accepted in ThreadPool
     */
    public LightFuture<T> thenApply(Function<T, T> function);
}
