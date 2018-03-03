package ru.spbau.mit.java.paradov;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.function.Function;
import java.util.function.Supplier;

public class ThreadPoolImpl<T> {
    private final ArrayList<Thread> threads = new ArrayList<>();
    private final Queue<ThreadTask> tasks = new LinkedList<>();

    public ThreadPoolImpl(int n) {
        for (int i = 0; i < n; i++) {
            threads.add(new Thread(new TaskDealer()));
            // Should I make it daemon???
            threads.get(i).start();
        }
    }

    public LightFuture<T> addTask(Supplier<T> supplier) {
        ThreadTask newTask = new ThreadTask(supplier);

        synchronized (tasks) {
            tasks.add(newTask);
            tasks.notify();
        }

        return newTask;
    }

    public void shutdown() {
        for (Thread t : threads) {
            t.interrupt();
        }
    }

    private class ThreadTask implements LightFuture<T> {
        private Supplier<T> supplier;
        private boolean isReady = false;
        private boolean isFinishedWithException = false;
        private Object result;

        private ThreadTask(Supplier<T> supplier) {
            this.supplier = supplier;
        }

        @Override
        public boolean isReady() {
            return isReady;
        }

        @Override
        public T get() throws LightExecutionException {
            while (!isReady) {
                // What to do, if someone shutdown ThreadPool while result was computing?
            }

            if (isFinishedWithException) {
                throw new LightExecutionException((Exception) result);
            }

            return (T) result;
        }

        @Override
        public <R> LightFuture<R> thenApply(Function<T, R> function) {
            return null;
        }

        private void computeResult() {
            try {
                result = supplier.get();
            } catch (Exception e) {
                result = e;
                isFinishedWithException = true;
            }

            isReady = true;
        }
    }


    private class TaskDealer implements Runnable {
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
