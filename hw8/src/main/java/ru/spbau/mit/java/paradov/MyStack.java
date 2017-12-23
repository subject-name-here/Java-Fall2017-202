package ru.spbau.mit.java.paradov;

import org.jetbrains.annotations.Nullable;

import java.util.NoSuchElementException;

/**
 * This is a stack. It does not extends java.util.Stack, because there are a lot of things
 * we don't use. Instead, here is a stack, that can do push() and pop(). When stack is full,
 * it is automatically increases its capacity, so we can push more.
 * @param <T> type of elements in stack
 */
public class MyStack<T> {
    /** Place where we keep all our elements. */
    private T[] storage;
    /** Number of elements in stack. */
    private int numOfElements = 0;
    /** Possible number of elements that we can keep without changing storage.*/
    private int capacity = 2;

    public MyStack() {
        storage = (T[]) new Object[capacity];
    }

    public int size() {
        return numOfElements;
    }

    public boolean isEmpty() {
        return size() == 0;
    }

    /**
     * Pops element from stack (deletes from its back) and returns it or null, if stack is empty.
     * @return element that was popped, or null, if stack was empty
     */
    public @Nullable T pop() throws NoSuchElementException {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }

        return storage[--numOfElements];
    }

    /**
     * Pushes element to stack (to its back). If stack is full, increases its capacity.
     * @param c element to push
     */
    public void push(T c) {
        if (numOfElements == capacity) {
            increaseCapacity();
        }

        storage[numOfElements++] = c;
    }

    /** Increases capacity of stack by multiplying storage size by 2. */
    private void increaseCapacity() {
        capacity *= 2;
        T[] new_storage = (T[]) new Object[capacity];

        System.arraycopy(storage, 0, new_storage, 0, numOfElements);

        storage = new_storage;
    }

    /** Reverses the stack by creating new storage and copying all elements in right order. */
    public void reverse() {
        T[] new_storage = (T[]) new Object[capacity];

        for (int i = 0; i < numOfElements; i++) {
            new_storage[numOfElements - i - 1] = storage[i];
        }

        storage = new_storage;
    }

}
