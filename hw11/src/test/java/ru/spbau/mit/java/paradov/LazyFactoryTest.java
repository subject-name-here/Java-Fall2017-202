package ru.spbau.mit.java.paradov;

import org.junit.Test;
import ru.spbau.mit.java.paradov.util.RunCounter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.function.Supplier;

import static org.junit.Assert.*;

/** Class for testing LazyFactory. */
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

    /* Tests if computation in supplier is called only once: when lazy.get() called first time. */
    @Test
    public void testSimpleLazyIsOptimal() {
        RunCounter supplier = new RunCounter();
        Lazy<Integer> lazy = LazyFactory.createSimpleLazy(supplier);

        assertEquals(0, (int) supplier.getCounter());
        assertEquals(1, (int) lazy.get());
        assertEquals(1, (int) supplier.getCounter());

        assertEquals(1, (int) lazy.get());
        assertEquals(1, (int) supplier.getCounter());
    }

    /* Tests if Lazy returns the same object as the first time. */
    @Test
    public void testSimpleLazyIsReturningTheSameResult() {
        RunCounter supplier = new RunCounter();
        Lazy<Integer> lazy = LazyFactory.createSimpleLazy(supplier);

        assertEquals(1, (int) lazy.get());
        supplier.get();

        assertEquals(2, (int) supplier.getCounter());
        assertEquals(1, (int) lazy.get());
    }

    /* Tests if Lazy is okay with returning null. */
    @Test
    public void testSimpleLazyNull() {
        Supplier<Integer> supplier = () -> null;
        Lazy<Integer> lazy = LazyFactory.createSimpleLazy(supplier);

        assertNull(lazy.get());
    }

    /**
     * Testing of multithreaded Lazy implementations.
     */

    /* Tests if multithreaded Lazy returns expected result. */
    @Test
    public void testMultiThreadedLazyIsCorrect() {
        Supplier<Integer> supplier = () -> 13;
        Lazy<Integer> lazy = LazyFactory.createMultiThreadedLazy(supplier);
        assertEquals(13, (int) lazy.get());
    }

    /* Tests if computation in supplier is called only once: when lazy.get() called first time. */
    @Test
    public void testMultiThreadedLazyIsOptimal() {
        RunCounter supplier = new RunCounter();
        Lazy<Integer> lazy = LazyFactory.createMultiThreadedLazy(supplier);

        assertEquals(0, (int) supplier.getCounter());
        assertEquals(1, (int) lazy.get());
        assertEquals(1, (int) supplier.getCounter());

        assertEquals(1, (int) lazy.get());
        assertEquals(1, (int) supplier.getCounter());
    }

    /* Tests if there is no race condition, and Lazy work nice with many threads. */
    @Test
    public void testMultiThreadedLazyWithManyThreads() throws InterruptedException {
        RunCounter supplier = new RunCounter();
        Lazy<Integer> lazy = LazyFactory.createMultiThreadedLazy(supplier);

        ArrayList<Thread> threads = new ArrayList<>();

        for (int i = 0; i < 1000; i++) {
            threads.add(new Thread(lazy::get));
        }

        for (Thread t : threads) {
            t.start();
        }

        for (Thread t : threads) {
            t.join();
        }

        assertEquals(1, (int) supplier.getCounter());
    }

    /* Big test on multithreaded Lazies. */
    @Test
    public void testManyMultiThreadedLazyWithManyThreads() throws InterruptedException {
        RunCounter supplier = new RunCounter();

        ArrayList<Lazy<Integer>> lazies = new ArrayList<>();
        ArrayList<Thread> threads = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            lazies.add(LazyFactory.createMultiThreadedLazy(supplier));
            for (int j = 0; j < 500; j++) {
                threads.add(new Thread(lazies.get(i)::get));
            }
        }
        Collections.shuffle(threads);

        for (Thread t : threads) {
            t.start();
        }

        for (Thread t : threads) {
            t.join();
        }

        assertEquals(lazies.size(), (int) supplier.getCounter());
    }

}