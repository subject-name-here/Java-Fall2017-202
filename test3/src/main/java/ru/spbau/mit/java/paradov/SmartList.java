package ru.spbau.mit.java.paradov;

import java.util.*;

/**
 * Array which allows to keep a small amount of objects as an array or even as a single object,
 * and big heaps of objects it keeps as ArrayList. If storage is empty, it keeps null.
 * If there is only one object, it keeps this object. If there is between 2 and 5 objects,
 * it keeps them in array of length 5. Otherwise, storage is an ArrayList.
 * @param <E> type of objects which we keep in storage
 */
public class SmartList<E> extends AbstractList<E> implements List<E> {
    /** Number of objects in storage. */
    private int size;

    /**
     * A place where we keep data. Depending on size, it can be null,
     * object that we keep, array of length 5 or ArrayList.
     */
    private Object storage;

    /** Constructs an empty list with null storage. */
    public SmartList() {
        size = 0;
        storage = null;
    }

    /**
     * Constructs a list containing the elements of the specified collection,
     * in the order they are returned by the collection's iterator.
     * @param collection the collection whose elements are to be placed into this list
     */
    public SmartList(Collection<? extends E> collection) {
        size = collection.size();

        if (collection.size() == 0) {
            storage = null;
        } else if (collection.size() == 1) {
            storage = collection.toArray()[0];
        } else if (collection.size() <= 5) {
            storage = collection.toArray();
        } else {
            storage = new ArrayList<>(collection);
        }
    }

    /**
     * Returns the number of elements in this list.
     * @return the number of elements in this list
     */
    @Override
    public int size() {
        return size;
    }

    /**
     * Returns the element at the specified position in this list.
     * @param i index of the element to return
     * @return the element at the specified position in this list
     * @throws IndexOutOfBoundsException if the index is out of range
     */
    @Override
    public E get(int i) throws IndexOutOfBoundsException {
        checkRange(i);

        if (size == 1) {
            return (E) storage;
        } else if (size <= 5) {
            return ((E[]) storage)[i];
        } else {
            return ((ArrayList<E>) storage).get(i);
        }
    }

    /**
     * Replaces the element at the specified position in this list with the specified element.
     * @param i index of the element to replace
     * @param e element to be stored at the specified position
     * @return the element previously at the specified position
     * @throws IndexOutOfBoundsException if the index is out of range
     */
    @Override
    public E set(int i, E e) throws IndexOutOfBoundsException {
        checkRange(i);
        E previousValue = get(i);

        if (size == 1) {
            storage = e;
        } else if (size <= 5) {
            ((E[]) storage)[i] = e;
        } else {
            ((ArrayList<E>) storage).set(i, e);
        }

        return previousValue;
    }

    /**
     * Inserts the specified element at the specified position in this list.
     * Shifts the element currently at that position (if any) and any subsequent elements
     * to the right (adds one to their indices).
     * @param i index at which the specified element is to be inserted
     * @param e element to be inserted
     * @throws IndexOutOfBoundsException if the index is out of range
     */
    @Override
    public void add(int i, E e) throws IndexOutOfBoundsException {
        if (i < 0 || i > size) {
            throw new IndexOutOfBoundsException();
        }

        if (size == 0) {
            storage = e;
        } else if (size < 5) {
            if (size == 1) {
                E oldStorage = (E) storage;
                storage = new Object[5];
                ((E[]) storage)[0] = oldStorage;
            }

            for (int cnt = size - 1; cnt >= i; cnt--) {
                ((E[]) storage)[cnt + 1] = ((E[]) storage)[cnt];
            }
            ((E[]) storage)[i] = e;
        } else {
            if (size == 5) {
                storage = new ArrayList<E>(Arrays.asList((E[]) storage));
            }
            ((ArrayList<E>) storage).add(i, e);
        }

        size++;
    }

    /**
     * Removes the element at the specified position in this list.
     * Shifts any subsequent elements to the left (subtracts one from their indices).
     * @param i the index of the element to be removed
     * @return the element that was removed from the list
     * @throws IndexOutOfBoundsException if the index is out of range
     */
    @Override
    public E remove(int i) throws IndexOutOfBoundsException {
        Object element = get(i);
        size--;

        if (size == 0) {
            storage = null;
        } else if (size < 5) {
            for (int cnt = i; cnt < size; cnt++) {
                ((E[]) storage)[cnt] = ((E[]) storage)[cnt + 1];
            }

            if (size == 1) {
                storage = ((E[]) storage)[0];
            }
        } else {
            ((ArrayList<E>) storage).remove(i);

            if (size == 5) {
                storage = ((ArrayList<E>) storage).toArray();
            }
        }

        return (E) element;
    }

    /** Removes all objects from list. */
    @Override
    public void clear() {
        size = 0;
        storage = null;
    }

    /**
     * Checks if given index is index of some element in list.
     * @param i index to check
     * @throws IndexOutOfBoundsException if index is out of range
     */
    private void checkRange(int i) throws IndexOutOfBoundsException {
        if (i < 0 || i >= size) {
            throw new IndexOutOfBoundsException();
        }
    }

}
