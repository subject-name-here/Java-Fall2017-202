package ru.spbau.mit.java.paradov.util;

import java.util.function.Supplier;

/**
 * Class for testing. It's just counter, which is getting increased when asked to supply.
 */
public class RunCounter implements Supplier<Integer> {
    /* Counter, that counts how many times method get() was called. */
    private Integer counter = 0;

    /**
     * Gets a number of calls to get(), including this one.
     * @return number of times method get() was called
     */
    @Override
    public Integer get() {
        return ++counter;
    }

    /**
     * An alternative for supplier's get(). It doesn't increase counter, so it used to check
     * how many times get() was called.
     * @return number of times method get() was called
     */
    public Integer getCounter() {
        return counter;
    }
}
