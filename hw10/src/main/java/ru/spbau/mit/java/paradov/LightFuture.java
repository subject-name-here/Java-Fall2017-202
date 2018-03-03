package ru.spbau.mit.java.paradov;

import java.util.function.Function;

public interface LightFuture<T> {

    public boolean isReady();

    public T get() throws LightExecutionException;

    public <R> LightFuture<R> thenApply(Function<T, R> function);
}
