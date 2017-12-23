package ru.spbau.mit.java.paradov;

import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * Implementation of MyTreeSet, based on AbstractSet. The elements are ordered using their
 * natural ordering, or by a Comparator provided at set creation time, depending on
 * which constructor is used. Because this implementation uses unbalanced binary search tree
 * as a data store, time cost for basic operations (add, remove, contains) can reach O(n).
 * @param <E> type of objects we keep in set
 */
public class UnbalancedBinaryTreeSet<E extends Comparable<? super E>>
        extends AbstractSet<E> implements MyTreeSet<E> {
    /** The tree in which we store data and which maintains all the structure. */
    private MyListedTree<E> tree;

    /**
     * Standard constructor of this class. Assigning to a comparator a standard version:
     * it just assumes that elements of set are Comparable and compares them.
     * If they are not, it will throw ClassCastException.
     */
    public UnbalancedBinaryTreeSet() {
        tree = new MyListedTree<>(Comparator.naturalOrder());
    }

    /**
     * Constructs a new, empty tree set, sorted according to the specified comparator.
     * @param comparator the comparator that will be used to order this set
     */
    public UnbalancedBinaryTreeSet(@NotNull Comparator<? super E> comparator) {
        tree = new MyListedTree<>(comparator);
    }

    /**
     * Returns an iterator over the elements in this set in descending order.
     * @return an iterator over the elements in this set in descending order
     */
    @Override
    public Iterator<E> descendingIterator() {
        return tree.iterator(true);
    }

    /**
     * Returns a reverse order view of the elements contained in this set.
     * The descending set is backed by this set, so changes to the set are reflected
     * in the descending set, and vice-versa.
     * @return a reverse order view of this set
     */
    @Override
    public MyTreeSet<E> descendingSet() {
        return new UnbalancedBinaryDescendingTreeSet();
    }

    /**
     * Returns the first (lowest) element currently in this set.
     * @return the first element currently in this set
     * @throws NoSuchElementException if called on empty set
     */
    @Override
    public E first() throws NoSuchElementException {
        if (tree.size() == 0)
            throw new NoSuchElementException();

        return tree.first();
    }

    /**
     * Returns the last (highest) element currently in this set.
     * @return the last element in this set
     * @throws NoSuchElementException if called on empty set
     */
    @Override
    public E last() throws NoSuchElementException {
        if (tree.size() == 0)
            throw new NoSuchElementException();

        return tree.last();
    }

    /**
     * Returns the greatest element in this set strictly less than the given element,
     * or null if there is no such element.
     * @param e value to match
     * @return the greatest element less than e, or null if there is no such element
     */
    @Override
    public E lower(E e) {
        return tree.lessThan(e);
    }

    /**
     * Returns the greatest element in this set less than or equal to the given element,
     * or null if there is no such element.
     * @param e value to match
     * @return the greatest element not greater than e, or null if there is no such element
     */
    @Override
    public E floor(E e) {
        return contains(e) ? e : lower(e);
    }

    /**
     * Returns the least element in this set greater than or equal to the given element,
     * or null if there is no such element.
     * @param e value to match
     * @return the least element not less e, or null if there is no such element
     */
    @Override
    public E ceiling(E e) {
        return contains(e) ? e : higher(e);
    }

    /**
     * Returns the least element in this set strictly greater than the given element,
     * or null if there is no such element.
     * @param e value to match
     * @return the least element greater than e, or null if there is no such element
     */
    @Override
    public E higher(E e) {
        return tree.greaterThan(e);
    }

    /**
     * Returns number of elements in this set.
     * @return number of elements in this set
     */
    @Override
    public int size() {
        return tree.size();
    }

    /**
     * Returns true if this set contains the specified element, false otherwise.
     * @param o object to be checked for containment in this set
     * @return true if this set contains the specified element
     */
    @Override
    public boolean contains(Object o) {
        return tree.contains(o);
    }

    /**
     * Returns iterator over this set in ascending order.
     * @return iterator over this set in ascending order
     */
    @Override
    public @NotNull Iterator<E> iterator() {
        return tree.iterator(false);
    }

    /**
     * Adds element to this set if it is not already present. If element is in list,
     * it does nothing and returns false.
     * @param e value to add
     * @return true, if element was added, false, if element already is in list
     */
    @Override
    public boolean add(E e) {
        return tree.add(e);
    }

    /**
     * Removes the specified element from this set if it is present. Returns true, if set
     * contained this element.
     * @param o object we want to remove from set
     * @return true, if object was in set and now deleted, false, if object was not in set
     */
    @Override
    public boolean remove(Object o) {
        return tree.remove(o);
    }

    /**
     * Removes all of the elements from this set. The set will be empty after this call returns.
     */
    @Override
    public void clear() {
        tree = new MyListedTree<>(tree.getComparator());
    }


    /**
     * Descending set which is totally connected to set from which we called method
     * descendingSet(). Almost every method is call of the same method of father-set.
     */
    private class UnbalancedBinaryDescendingTreeSet extends UnbalancedBinaryTreeSet<E> {
        /**
         * Returns an iterator over the elements in this set in descending order,
         * or more precisely, an iterator over the elements in father-set in ascending order.
         * @return an iterator over the elements in this set in descending order
         */
        @Override
        public Iterator<E> descendingIterator() {
            return UnbalancedBinaryTreeSet.this.iterator();
        }

        /**
         * Returns a reverse order view of the elements contained in this set, or
         * more precisely, returns father-set (because it is equivalent to what we want).
         * @return set with reverse order view of elements
         */
        @Override
        public MyTreeSet<E> descendingSet() {
            return UnbalancedBinaryTreeSet.this;
        }

        /**
         * Returns the first (lowest) element currently in this set.
         * @return the first element currently in this set
         */
        @Override
        public E first() {
            return UnbalancedBinaryTreeSet.this.last();
        }

        /**
         * Returns the last (highest) element currently in this set.
         * @return the last element currently in this set
         */
        @Override
        public E last() {
            return UnbalancedBinaryTreeSet.this.first();
        }

        /**
         * Returns the greatest element in this set strictly less than the given element,
         * or null if there is no such element.
         * @param e value to match
         * @return the greatest element less than e, or null if there is no such element
         */
        @Override
        public E lower(E e) {
            return UnbalancedBinaryTreeSet.this.higher(e);
        }

        /**
         * Returns the greatest element in this set less than or equal to the given element,
         * or null if there is no such element.
         * @param e value to match
         * @return the greatest element not greater than e, or null if there is no such element
         */
        @Override
        public E floor(E e) {
            return UnbalancedBinaryTreeSet.this.ceiling(e);
        }

        /**
         * Returns the least element in this set greater than or equal to the given element,
         * or null if there is no such element.
         * @param e value to match
         * @return the least element not less e, or null if there is no such element
         */
        @Override
        public E ceiling(E e) {
            return UnbalancedBinaryTreeSet.this.floor(e);
        }

        /**
         * Returns the least element in this set strictly greater than the given element,
         * or null if there is no such element.
         * @param e value to match
         * @return the least element greater than e, or null if there is no such element
         */
        @Override
        public E higher(E e) {
            return UnbalancedBinaryTreeSet.this.lower(e);
        }

        /**
         * Returns number of elements in this set.
         * @return number of elements in this set
         */
        @Override
        public int size() {
            return UnbalancedBinaryTreeSet.this.size();
        }

        /**
         * Returns true if this set contains the specified element, false otherwise.
         * @param o object to be checked for containment in this set
         * @return true if this set contains the specified element
         */
        @Override
        public boolean contains(Object o) {
            return UnbalancedBinaryTreeSet.this.contains(o);
        }

        /**
         * Returns an iterator over the elements in this set in ascending order.
         * @return an iterator over the elements in this set in ascending order
         */
        @Override
        public @NotNull Iterator<E> iterator() {
            return UnbalancedBinaryTreeSet.this.descendingIterator();
        }

        /**
         * Adds a value to set. Actually, it adds value to father-set.
         * @param e value to add
         * @return true, if value was added, false, if this value already was in set
         */
        @Override
        public boolean add(E e) {
            return UnbalancedBinaryTreeSet.this.add(e);
        }

        /**
         * Removes object from set by removing it from father-set.
         * @param o object we want to remove from set
         * @return true, if object was removed, false, if object was not in set
         */
        @Override
        public boolean remove(Object o) {
            return UnbalancedBinaryTreeSet.this.remove(o);
        }

        /**
         * Delete all objects from father-set.
         */
        @Override
        public void clear() {
            UnbalancedBinaryTreeSet.this.clear();
        }
    }
}
