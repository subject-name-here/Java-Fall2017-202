package ru.spbau.mit.java.paradov;

import org.junit.Test;

import java.util.function.Supplier;

import static org.junit.Assert.*;

public class LazyFactoryTest {
    /**
     * Testing of single-threaded Lazy implementations.
     */

    /* Tests if simple single-threaded Lazy returns expected result. */
    @Test
    public void testSimpleLazyCorrect() {
        Supplier<Integer> supplier = () -> 13;
        Lazy<Integer> lazy = LazyFactory.createSimpleLazy(supplier);
        assertEquals(13, (int) lazy.get());
    }

    @Test
    public void testSimpleLazyIsOptimal() {
        LaunchCounter supplier = new LaunchCounter();
        Lazy<Integer> lazy = LazyFactory.createSimpleLazy(supplier);

        assertEquals(0, (int) supplier.getCounter());
        assertEquals(1, (int) lazy.get());
        assertEquals(1, (int) supplier.getCounter());

        assertEquals(1, (int) lazy.get());
        assertEquals(1, (int) supplier.getCounter());
    }


    /**
     * Testing of multithreaded Lazy implementations.
     */

    @Test
    public void testCreateMultiThreadedLazy() throws Exception {
    }



}