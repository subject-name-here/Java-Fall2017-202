package ru.spbau.mit.java.paradov;

import org.junit.Test;
import ru.spbau.mit.java.paradov.util.SupplierCollection;

import java.util.ArrayList;

import static org.junit.Assert.*;

/** Tests of ThreadPoolImpl class. */
public class ThreadPoolImplTest {
    /** Tests if LightFuture.get() works correctly. */
    @Test
    public void testGet() throws Exception {
        ThreadPoolImpl<Integer> pool = new ThreadPoolImpl<>(3);
        LightFuture<Integer> result = pool.addTask(SupplierCollection.returnBasicSupplier(42));
        assertEquals(42, (int) result.get());
        pool.shutdown();
    }

    /** Tests if LightFuture.get() works correctly when it needs to wait. */
    @Test
    public void testGetIsWaiting() throws Exception {
        ThreadPoolImpl<Integer> pool = new ThreadPoolImpl<>(3);
        LightFuture<Integer> result = pool.addTask(SupplierCollection.returnTimeSupplier(2));
        assertEquals(2, (int) result.get());
        pool.shutdown();
    }

    /** Tests if ThreadPoolImpl doesn't throw exceptions if exception was thrown in task. */
    @Test
    public void testSupplierWithException() {
        ThreadPoolImpl<Integer> pool = new ThreadPoolImpl<>(3);
        LightFuture<Integer> result = pool.addTask(SupplierCollection.returnExceptionSupplier());
        pool.shutdown();
    }

    /** Tests that LightFuture.get() throws exception when it's expected. */
    @Test(expected = LightExecutionException.class)
    public void testGetWithException() throws Exception {
        ThreadPoolImpl<Integer> pool = new ThreadPoolImpl<>(3);
        LightFuture<Integer> result = pool.addTask(SupplierCollection.returnExceptionSupplier());
        result.get();
        pool.shutdown();
    }

    /** Tests how ThreadPoolImpl works with many tasks. */
    @Test
    public void testMultitasking() throws Exception {
        ThreadPoolImpl<Integer> pool = new ThreadPoolImpl<>(5);
        ArrayList<LightFuture<Integer>> tasks = new ArrayList<>();
        for (int i = 0; i < 15; i++) {
            tasks.add(pool.addTask(SupplierCollection.returnBasicSupplier(i)));
        }

        for (int i = 0; i < 15; i++) {
            assertEquals(i, (int) tasks.get(i).get());
        }
        pool.shutdown();
    }

    /** Tests if LightFuture.thenApply() works correctly. */
    @Test
    public void testMethodThenApply() throws Exception {
        ThreadPoolImpl<Integer> pool = new ThreadPoolImpl<>(33);
        LightFuture<Integer> task = pool.addTask(SupplierCollection.returnBasicSupplier(3));
        LightFuture<Integer> result = task.thenApply(x -> x * x + 3 * x + 4);
        assertEquals(22, (int) result.get());
        pool.shutdown();
    }

    /** Tests if LightFuture.thenApply() works correctly when it needs to wait. */
    @Test
    public void testMethodThenApplyIsWaiting() throws Exception {
        ThreadPoolImpl<Integer> pool = new ThreadPoolImpl<>(3);
        LightFuture<Integer> task = pool.addTask(SupplierCollection.returnTimeSupplier(3));
        LightFuture<Integer> result = task.thenApply(x -> x * x * x + 1);
        assertEquals(28, (int) result.get());
        pool.shutdown();
    }

    /** Tests some real parallel work: sum from 0 to amount * t. */
    @Test
    public void testArrayOperationsParallel() throws Exception {
        ThreadPoolImpl<Long> pool = new ThreadPoolImpl<>(4);
        ArrayList<LightFuture<Long>> tasks = new ArrayList<>();
        final Long t = 250_000_000L;
        final int amount = 4;

        for (int i = 0; i < amount; i++) {
            final int index = i;
            tasks.add(pool.addTask(() -> {
                Long result = 0L;
                for (Long j = t * index; j < t * (index + 1); j++) {
                    result += j;
                }
                return result;
            }));
        }

        for (int i = 0; i < amount; i++) {
            // This formula is checked twice and it's correct for sure.
            assertEquals(t * (t / 2) * (2 * i + 1) - t / 2, (Object) tasks.get(i).get());
        }
        Long finalResult = tasks.stream().mapToLong(lf -> {
            try {
                return lf.get();
            } catch (LightExecutionException e) {
                return t * t * amount * amount; // bigger than expected result
            }
        }).sum();
        assertEquals((t * amount - 1) * (t * amount) / 2, (Object) finalResult);

        /*
        Fact: with many threads computation is faster than this straight-forward code:
        Long result = 0L;
        for (Long i = 0L; i < 1e9; i++) {
            result += i;
        }
        */
        pool.shutdown();
    }

}