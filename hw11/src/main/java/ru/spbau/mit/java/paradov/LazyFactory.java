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
        /** Flag that signals if second field is storing result. If false, it's supplier. */
        private boolean isResult;

        /**
         * If field isResult == false, then it stores supplier that will provide result on demand.
         * Otherwise, it keeps result that supplier gave.
         */
        private Object supplierOrResult;

        /**
         * Constructor of SimpleLazy, initializing value field by given supplier.
         * @param supplier given supplier
         */
        private SimpleLazy(Supplier<T> supplier) {
            supplierOrResult = supplier;
            isResult = false;
        }

        /**
         * Gets a result. First time, it launches provided supplier. Next times, it just returns
         * the same object as it returned the first time.
         * @return a result
         */
        @Override
        @Nullable
        public T get() {
            if (!isResult) {
                supplierOrResult = ((Supplier<T>) supplierOrResult).get();
                isResult = true;
            }

            return (T) supplierOrResult;
        }
    }

    /**
     * Implementation of Lazy that guarantees correct Lazy work in multithreaded node.
     * @param <T> type of object supplier returns
     */
    private static class MultiThreadedLazy<T> implements Lazy<T> {
        /** Flag that signals if second field is storing result. If false, it's supplier. */
        private boolean isResult;

        /**
         * If field isResult == false, then it stores supplier that will provide result on demand.
         * Otherwise, it keeps result that supplier gave.
         */
        volatile private Object supplierOrResult;

        /**
         * Constructor of MultiThreadedLazy, initializing value field by given supplier.
         * @param supplier given supplier
         */
        private MultiThreadedLazy(Supplier<T> supplier) {
            supplierOrResult = supplier;
            isResult = false;
        }

        /**
         * Gets a result. First time, it launches provided supplier. Next times, it just returns
         * the same object as it returned the first time.
         * @return a result
         */
        @Override
        @Nullable
        public T get() {
            if (!isResult) {
                synchronized (this) {
                    if (!isResult) {
                        supplierOrResult = ((Supplier<T>) supplierOrResult).get();
                        isResult = true;
                    }
                }
            }

            return (T) supplierOrResult;
        }
    }
}
