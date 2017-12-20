package ru.spbau.mit.java.paradov;

import java.util.*;

public class SmartList<E> extends AbstractList<E> implements List<E> {
    private int size;
    private Object storage;

    public SmartList() {
        size = 0;
        storage = null;
    }

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

    @Override
    public int size() {
        return size;
    }

    @Override
    public E get(int i) {
        checkRange(i);

        if (size == 1) {
            return (E) storage;
        } else if (size <= 5) {
            return ((E[]) storage)[i];
        } else {
            return ((ArrayList<E>) storage).get(i);
        }
    }

    @Override
    public E set(int i, E e) {
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

    @Override
    public void add(int i, E e) {
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

    @Override
    public E remove(int i) {
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

    @Override
    public void clear() {
        size = 0;
        storage = null;
    }

    private void checkRange(int i) {
        if (i < 0 || i >= size) {
            throw new IndexOutOfBoundsException();
        }
    }

}
