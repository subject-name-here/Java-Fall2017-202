package ru.spbau.mit.java.paradov.util;

import java.util.function.Supplier;

/**
 * Collection of Suppliers for tests of ThreadPoolImpl.
 */
public class SupplierCollection {
    public static Supplier<Integer> returnBasicSupplier(Integer i) {
        return () -> i;
    }
}
