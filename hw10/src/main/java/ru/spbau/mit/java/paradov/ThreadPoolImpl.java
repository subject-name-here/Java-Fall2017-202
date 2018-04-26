package ru.spbau.mit.java.paradov;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Task pool with fixed number of threads (number is given in constructor).
 * Every thread is waiting for a task, then it computes some value and returns to waiting.
 * @param <T> type of tasks results
 */
public class ThreadPoolImpl<T> {
    /**
     * ArrayList of threads. Once ThreadPool is constructed, this list is filled with
     * running threads.
     */
    private final ArrayList<Thread> threads = new ArrayList<>();
    /**
     * Task pool, where we can add tasks to and where from threads take tasks.
     */
    private final Queue<ThreadTask> tasks = new LinkedList<>();

    /**
     * Constructs new ThreadPool with given number of running threads.
     * @param n number of threads to run in ThreadPool
     */
    public ThreadPoolImpl(int n) {
        for (int i = 0; i < n; i++) {
            threads.add(new Thread(new TaskDealer()));
            threads.get(i).start();
        }
    }

    /**
     * Adds given supplier as a task to ThreadPool.
     * @param supplier task to be added to the pool
     * @return object LightFuture - wrap around supplier - added task
     */
    public LightFuture<T> addTask(Supplier<T> supplier) {
        ThreadTask newTask = new ThreadTask(supplier);

        synchronized (tasks) {
            tasks.add(newTask);
            tasks.notify();
        }

        return newTask;
    }

    /**
     * Interrupts all threads in this ThreadPool. Changes flag, so all LightFuture objects,
     * which wait for result, know that result will not be provided.
     */
    public void shutdown() {
        for (Thread t : threads) {
            t.interrupt();
        }
    }

    /**
     * Implementation of LightFuture - class for tasks that are taken by this ThreadPool.
     */
    private class ThreadTask implements LightFuture<T> {
        /** Supplier that provides value to compute. */
        private Supplier<T> supplier;
        /** Status of computation. If exception was thrown, computation is considered as finished. */
        private boolean isComputationOver = false;
        /** It is a result provided by supplier. */
        private T result;
        /** It is an exception thrown by supplier. */
        private Exception exception;

        /** Monitor object to warn method get() when computation is done. */
        private final Object monitor = new Object();

        /**
         * Constructs task from given supplier.
         * @param supplier supplier that provides value to compute
         */
        private ThreadTask(Supplier<T> supplier) {
            this.supplier = supplier;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public boolean isReady() {
            return isComputationOver;
        }

        /**
         * Returns result of computation, when it's done. If thread was interrupted while
         * waiting, it saves exception as a result.
         * @return result of computation
         * @throws LightExecutionException if providing supplier has finished work with exception
         */
        @Override
        public T get() throws LightExecutionException {
            synchronized (monitor) {
                try {
                    while (!isReady()) {
                        monitor.wait();
                    }
                } catch (InterruptedException e) {
                    isComputationOver = true;
                    exception = new Exception("Computation wasn't completed: " +
                            "ThreadPool was shutdown.");
                }
            }

            if (exception != null) {
                throw new LightExecutionException(exception);
            }

            return result;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public LightFuture<T> thenApply(Function<T, T> function) {
            return ThreadPoolImpl.this.addTask(() -> {
                try {
                    return function.apply(get());
                } catch (LightExecutionException e) {
                    throw new RuntimeException(e);
                }
            });
        }

        /**
         * Gets value from supplier and saves it. If it throws an exception, then it saves
         * this exception as a result.
         */
        private void computeResult() {
            synchronized (monitor) {
                try {
                    result = supplier.get();
                } catch (Exception e) {
                    exception = e;
                }

                isComputationOver = true;
                monitor.notifyAll();
            }
        }
    }


    /**
     * Runnable implementation for ThreadPoolImpl to run. They are waiting for tasks,
     * and when they grabbed one, they compute it and wait again.
     */
    private class TaskDealer implements Runnable {

        /**
         * {@inheritDoc}
         */
        @Override
        public void run() {
            while (!Thread.interrupted()) {
                ThreadTask task;

                synchronized (tasks) {
                    try {
                        while (tasks.isEmpty()) {
                            tasks.wait();
                        }
                    } catch (InterruptedException e) {
                        break;
                    }

                    task = tasks.remove();
                }

                task.computeResult();
            }
        }
    }

}
