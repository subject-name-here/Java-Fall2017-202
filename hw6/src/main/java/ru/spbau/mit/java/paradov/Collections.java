package ru.spbau.mit.java.paradov;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

/** Class that contains operations to work with functions and container. */
public class Collections {
    /**
     * Applies function to all elements in the container.
     * Results are put in ArrayList, which we return.
     * @param f function that we are applying
     * @param container container with elements we are applying function to
     * @param <T> type of elements in container
     * @param <R> type of function's applying result and type of elements in container we return
     * @return ArrayList with results of applying function to element from given container
     */
    public static <T, R> @NotNull ArrayList<R> map(@NotNull Function1<T, R> f,
                                                   @NotNull Iterable<T> container){
        ArrayList<R> result = new ArrayList<>();
        for (T obj : container) {
            result.add(f.apply(obj));
        }

        return result;
    }

    /**
     * Creates ArrayList with elements from given container, predicate on which returns true.
     * @param predicate predicate which checks if we put element in result container
     * @param container container with elements we choose from
     * @param <T> type of elements in container
     * @return ArrayList with elements from given container, predicate on which returns true
     */
    public static <T> @NotNull ArrayList<T> filter(@NotNull Predicate<T> predicate,
                                                   @NotNull Iterable<T> container){
        ArrayList<T> result = new ArrayList<>();
        for (T obj : container) {
            if (predicate.apply(obj)) {
                result.add(obj);
            }
        }

        return result;
    }

    /**
     * Creates ArrayList with elements from given container, from beginning to the element,
     * predicate on which have returned true, and either it was last element,
     * or predicate on next element have returned false.
     * @param predicate predicate which we use to stop taking elements
     * @param container container with elements we want to check
     * @param <T> type of elements in container
     * @return ArrayList with elements from given container, from beginning to the first element,
     * predicate on which have returned false, without this element
     */
    public static <T> @NotNull ArrayList<T> takeWhile(@NotNull Predicate<T> predicate,
                                                      @NotNull Iterable<T> container){
        ArrayList<T> result = new ArrayList<>();
        for (T obj : container) {
            if (predicate.apply(obj)) {
                result.add(obj);
            } else {
                break;
            }
        }

        return result;
    }

    /**
     * Creates ArrayList with elements from given container, from beginning to the element,
     * predicate on which have returned false, and either it was last element,
     * or predicate on next element have returned true.
     * @param predicate predicate which we use to stop taking elements
     * @param container container with elements we want to check
     * @param <T> type of elements in container
     * @return ArrayList with elements from given container, from beginning to the first element,
     * predicate on which have returned true, without this element
     */
    public static <T> @NotNull ArrayList<T> takeUnless(@NotNull Predicate<T> predicate,
                                                       @NotNull Iterable<T> container){
        return takeWhile(predicate.not(), container);
    }

    /**
     * Turns a list to one value by combining the result of recursively combining
     * all elements but the last one, with the last element, using given function.
     * @param f function we use to combine elements
     * @param ini initial value; result of combining zero elements
     * @param collection collection which contains elements we combine
     * @param <T> type of elements in container
     * @param <R> type of result value
     * @return result of combining collection into value
     */
    public static <T, R> R foldl(@NotNull Function2<R, T, R> f,
                                 R ini,
                                 @NotNull Collection<T> collection) {
        R result = ini;
        for (T obj : collection) {
            result = f.apply(result, obj);
        }

        return result;
    }

    /**
     * Turns a list to one value by combining the first element
     * with the result of recursively combining the rest, using given function.
     * @param f function we use to combine elements
     * @param ini initial value; result of combining zero elements
     * @param collection collection which contains elements we combine
     * @param <T> type of elements in container
     * @param <R> type of result value
     * @return result of combining collection into value
     */
    public static <T, R> R foldr(@NotNull Function2<T, R, R> f,
                                 R ini,
                                 @NotNull Collection<T> collection) {
        return foldrWithIterator(f, ini, collection.iterator());
    }

    /**
     * Utility function that we call from foldr(), because in this function we can do
     * recursion on iterators, not on collection.
     * @param f function we use to combine elements
     * @param ini initial value; result of combining zero elements
     * @param iterator iterator to an element we want to combine
     * @param <T> type of elements in container
     * @param <R> type of result value
     * @return result of combining collection into value
     */
    private static <T, R> R foldrWithIterator(@NotNull Function2<T, R, R> f,
                                              R ini,
                                              @NotNull Iterator<T> iterator) {
        if (!iterator.hasNext()) {
            return ini;
        }

        T obj = iterator.next();
        return f.apply(obj, foldrWithIterator(f, ini, iterator));
    }

}
