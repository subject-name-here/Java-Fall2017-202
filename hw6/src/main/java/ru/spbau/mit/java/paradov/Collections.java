package ru.spbau.mit.java.paradov;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/** Class that contains operations to work with functions and container. */
public class Collections {
    /**
     * Applies function to all elements in the container.
     * Results are put in ArrayList, which we return.
     * @param f function that we are applying
     * @param container container with elements we are applying function to
     * @param <T> type of elements that function takes
     * @param <R> type of function's applying result
     * @return list with results of applying function to element from given container
     */
    public static <T, R> @NotNull List<R> map(@NotNull final Function1<? super T, ? extends R> f,
                                              @NotNull final Iterable<? extends T> container){
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
     * @return list with elements from given container, predicate on which returns true
     */
    public static <T> @NotNull List<T> filter(@NotNull final Predicate<? super T> predicate,
                                              @NotNull final Iterable<? extends T> container){
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
     * @return list with elements from given container, from beginning to the first element,
     * predicate on which have returned false, without this element
     */
    public static <T> @NotNull List<T> takeWhile(@NotNull final Predicate<? super T> predicate,
                                                 @NotNull final Iterable<? extends T> container){
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
     * @return list with elements from given container, from beginning to the first element,
     * predicate on which have returned true, without this element
     */
    public static <T> @NotNull List<T> takeUnless(@NotNull final Predicate<? super T> predicate,
                                                  @NotNull final Iterable<? extends T> container){
        return takeWhile(predicate.not(), container);
    }

    /**
     * Turns a list to one value by combining the result of recursively combining
     * all elements but the last one, with the last element, using given function.
     * @param f function we use to combine elements
     * @param initialValue initial value; result of combining zero elements
     * @param collection collection which contains elements we combine
     * @param <T> type of elements in container
     * @param <R> type of result value
     * @return result of combining collection into value
     */
    public static <T, R> R foldl(@NotNull final Function2<? super R, ? super T, ? extends R> f,
                                 final R initialValue,
                                 @NotNull final Collection<? extends T> collection) {
        R result = initialValue;
        for (T obj : collection) {
            result = f.apply(result, obj);
        }

        return result;
    }

    /**
     * Turns a list to one value by combining the first element
     * with the result of recursively combining the rest, using given function.
     * @param f function we use to combine elements
     * @param initialValue initial value; result of combining zero elements
     * @param collection collection which contains elements we combine
     * @param <T> type of elements in container
     * @param <R> type of result value
     * @return result of combining collection into value
     */
    public static <T, R> R foldr(@NotNull final Function2<? super T, ? super R, ? extends R> f,
                                 final R initialValue,
                                 @NotNull final Collection<? extends T> collection) {
        return foldrWithIterator(f, initialValue, collection.iterator());
    }

    /**
     * Utility function that we call from foldr(), because in this function we can do
     * recursion on iterators, not on collection.
     * @param f function we use to combine elements
     * @param initialValue initial value; result of combining zero elements
     * @param iterator iterator to an element we want to combine
     * @param <T> type of elements in container
     * @param <R> type of result value
     * @return result of combining collection into value
     */
    private static <T, R> R foldrWithIterator(@NotNull final Function2<? super T, ? super R, ? extends R> f,
                                              final R initialValue,
                                              @NotNull final Iterator<? extends T> iterator) {
        if (!iterator.hasNext()) {
            return initialValue;
        }

        T obj = iterator.next();
        return f.apply(obj, foldrWithIterator(f, initialValue, iterator));
    }

}
