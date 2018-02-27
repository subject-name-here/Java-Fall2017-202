package ru.spbau.mit.java.paradov.util;

import java.util.function.Supplier;

public class RunCounter implements Supplier<Integer> {
    private Integer counter = 0;

    @Override
    public Integer get() {
        return ++counter;
    }

    public Integer getCounter() {
        return counter;
    }
}
