package ru.spbau.mit.java.paradov;

import java.util.function.Supplier;

public class LaunchCounter implements Supplier<Integer> {
    private Integer counter = 0;

    @Override
    public Integer get() {
        return ++counter;
    }

    public Integer getCounter() {
        return counter;
    }
}
