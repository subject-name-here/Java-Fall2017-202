package ru.spbau.mit.java.paradov;

import java.util.function.Supplier;

public class LazyFactory {
    public static <T> Lazy<T> createSimpleLazy(Supplier<T> supplier) {
        return new SimpleLazy<T>(supplier);
    }

    public static <T> Lazy<T> createMultiThreadLazy(Supplier<T> supplier) {
        return new MultiThreadLazy<T>(supplier);
    }

    private static class SimpleLazy<T> implements Lazy<T> {
        private Supplier<T> supplier;

        private T result;

        private SimpleLazy(Supplier<T> supplier) {
            this.supplier = supplier;
        }

        @Override
        public T get() {
            if (supplier == null) {
                return result;
            }

            result = supplier.get();
            supplier = null;
            return result;
        }
    }

    private static class MultiThreadLazy<T> implements Lazy<T> {
        private String state;

        private Object supplierOrResult;

        private MultiThreadLazy(Supplier<T> supplier) {
            supplierOrResult = supplier;
            state = new String("Supplier");
        }

        @Override
        public T get() {
            if (state.equals("Supplier")) {
                synchronized (state) {
                    if (state.equals("Supplier")) {
                        supplierOrResult = ((Supplier<T>) supplierOrResult).get();
                        state = new String("Result");
                    }
                }
            }

            return (T) supplierOrResult;
        }
    }
}
