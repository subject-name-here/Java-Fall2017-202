package ru.spbau.mit.java.paradov;

import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

/**
 * Creates simple and multithreaded implementations of Lazy.
 */
public class LazyFactory {
    /**
     * Returns Lazy supplier with guarantee of correct work in single-threaded mode.
     * @param supplier supplier that will be wrapped in Lazy
     * @param <T> type of object supplier returns
     * @return Lazy object, based on given supplier
     */
    public static <T> Lazy<T> createSimpleLazy(Supplier<T> supplier) {
        return new SimpleLazy<>(supplier);
    }

    /**
     * Returns Lazy supplier with guarantee of correct work in multithreaded mode.
     * @param supplier supplier that will be wrapped in Lazy
     * @param <T> type of object supplier returns
     * @return Lazy object, based on given supplier
     */
    public static <T> Lazy<T> createMultiThreadedLazy(Supplier<T> supplier) {
        return new MultiThreadedLazy<>(supplier);
    }

    /**
     * Implementation of Lazy that guarantees correct Lazy work in single-threaded node.
     * @param <T> type of object supplier returns
     */
    private static class SimpleLazy<T> implements Lazy<T> {
        /** Result, provided by supplier. */
        private T result;
        /**
         * Supplier that provides result. When result is saved, supplier is becoming null,
         * so we can detect if computation was done.
         */
        private Supplier<T> supplier;

        /**
         * Constructor of SimpleLazy, initializing value field by given supplier.
         * @param supplier given supplier
         */
        private SimpleLazy(Supplier<T> supplier) {
            this.supplier = supplier;
        }

        /**
         * Gets a result. First time, it launches provided supplier. Next times, it just returns
         * the same object as it returned the first time.
         * @return a result
         */
        @Override
        @Nullable
        public T get() {
            if (supplier != null) {
                result = supplier.get();
                supplier = null;
            }

            return result;
        }
    }

    /**
     * Implementation of Lazy that guarantees correct Lazy work in multithreaded node.
     * @param <T> type of object supplier returns
     */
    private static class MultiThreadedLazy<T> implements Lazy<T> {
        /** Result, provided by supplier. */
        private volatile T result;
        /**
         * Supplier that provides result. When result is saved, supplier is becoming null,
         * so we can detect if computation was done.
         */
        private volatile Supplier<T> supplier;

        /**
         * Constructor of MultiThreadedLazy, initializing value field by given supplier.
         * @param supplier given supplier
         */
        private MultiThreadedLazy(Supplier<T> supplier) {
            this.supplier = supplier;
        }

        /**
         * Gets a result. First time, it launches provided supplier. Next times, it just returns
         * the same object as it returned the first time.
         * @return a result
         */
        @Override
        @Nullable
        public T get() {
            if (supplier != null) {
                synchronized (this) {
                    if (supplier != null) {
                        result = supplier.get();
                        supplier = null;
                    }
                }
            }

            return result;
        }
    }
}
