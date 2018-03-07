package ru.spbau.mit.java.paradov;

import org.junit.Test;
import ru.spbau.mit.java.paradov.util.SupplierCollection;

import static org.junit.Assert.*;

public class ThreadPoolImplTest {

    @Test
    public void testGet() throws LightExecutionException {
        ThreadPoolImpl<Integer> pool = new ThreadPoolImpl<>(5);
        LightFuture<Integer> result = pool.addTask(SupplierCollection.returnBasicSupplier(42));
        assertEquals(42, (int) result.get());
        pool.shutdown();
    }
}