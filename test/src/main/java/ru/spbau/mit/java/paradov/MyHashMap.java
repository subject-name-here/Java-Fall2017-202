package ru.spbau.mit.java.paradov;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import static java.lang.Math.abs;

public class MyHashMap<K, V> implements Map, Iterable<Map.Entry<K, V>> {
    private int size = 0;

    private MyList[] table;

    private MyList allElements;

    private int numOfElements = 0;

    private int numOfLists = 16;

    private final int MAX_SIZE_PER_LIST = 16;

    public MyHashMap() {
        table = new MyList[numOfLists];

        for (int i = 0; i < numOfLists; i++) {
            table[i] = new MyList();
        }
    }

    private int hash(Object key, int mod) {
        return abs(key.hashCode()) % mod;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean containsKey(Object o) {
        return table[hash(o, numOfLists)].contains(o);
    }

    @Override
    public boolean containsValue(Object o) {
        for (int i = 0; i < numOfLists; i++) {
            if (table[i].containsValue(o)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Object get(Object o) {
        return table[hash(o, numOfLists)].get(o);
    }

    @Override
    public Object put(Object o, Object o2) {
        if (numOfElements > MAX_SIZE_PER_LIST * numOfLists)
            resize(MAX_SIZE_PER_LIST * numOfLists);

        Object toReturn = table[hash(o, numOfLists)].put(o, o2);
        if (toReturn == null) {
            numOfElements++;
        }

        return toReturn;
    }

    @Override
    public Object remove(Object o) {
        Object toReturn = table[hash(o, numOfLists)].remove(o);

        if (toReturn != null)
            numOfElements--;

        return toReturn;
    }

    @Override
    public void putAll(Map map) {

    }

    @Override
    public void clear() {
        table = new MyList[numOfLists];

        for (int i = 0; i < numOfLists; i++) {
            table[i] = new MyList();
        }

        numOfElements = 0;
    }

    private void resize(int newNumOfLists) {
        MyList[] newTable = new MyList[newNumOfLists];

        for (int i = 0; i < newNumOfLists; i++) {
            newTable[i] = new MyList();
        }

        for (int i = 0; i < numOfLists; i++) {
            while (table[i].getHead() != null) {
                int newListNum = hash(table[i].getHeadKey(), newNumOfLists);
                newTable[newListNum].put(table[i].getHeadKey(), table[i].getHeadValue());

                table[i].remove(table[i].getHeadKey());
            }
        }

        table = newTable;
        numOfLists = newNumOfLists;
    }

    @Override
    public Set keySet() {
        return null;
    }

    @Override
    public Collection values() {

        return null;
    }

    @Override
    public Set<Entry> entrySet() {
        return null;
    }

    @Override
    public Iterator<Entry<K, V>> iterator() {
        return null;
    }
}
