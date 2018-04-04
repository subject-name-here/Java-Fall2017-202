package ru.spbau.mit.java.paradov.util;

import java.util.function.Supplier;

/**
 * Collection of Suppliers for tests of ThreadPoolImpl.
 */
public class SupplierCollection {
    /**
     * Returns supplier, that returns given parameter.
     * @param i given parameter
     * @return supplier that always returns given parameter
     */
    public static Supplier<Integer> returnBasicSupplier(Integer i) {
        return () -> i;
    }

    /**
     * Supplier that sleeps (given parameter) seconds, then returns given parameter.
     * @param i seconds to sleep
     * @return supplier that always returns given parameter
     */
    public static Supplier<Integer> returnTimeSupplier(Integer i) {
        try {
            Thread.sleep(i * 1000);
        } catch (InterruptedException e) {
            throw new RuntimeException("Supplier was interrupted!");
        }
        return () -> i;
    }

    /**
     * Returns supplier that throws RuntimeException.
     * @return supplier that throws exception
     */
    public static Supplier<Integer> returnExceptionSupplier() {
        return () -> 42 / 0;
    }
}
