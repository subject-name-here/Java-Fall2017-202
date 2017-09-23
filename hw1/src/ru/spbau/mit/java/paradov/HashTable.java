package ru.spbau.mit.java.paradov;

import static java.lang.Math.abs;

/** Хэш-таблица с закрытой адресацией (на самодельных списках). */
public class HashTable {

    /** Массив листов, в которых хранятся данные. */
    private MyList[] table;

    /** Количество элементов в таблице. */
    private int numOfElements = 0;

    /** Количество листов в таблице. (при создании - 16) */
    private int numOfLists = 16;

    /** Константа, определяющая, когда количество элементов становится слишком большим. */
    private final int MAX_SIZE_PER_LIST = 16;

    /** Создает таблицу с 16 списками. */
    public HashTable() {
        table = new MyList[numOfLists];

        for (int i = 0; i < numOfLists; i++) {
            table[i] = new MyList();
        }
    }

    /**
     * Возвращает количество элементов в таблице.
     *
     * @return количество элементов в таблице
     */
    public int size() {
        return numOfElements;
    }

    /**
     * Проверяет, содержится ли элемент с ключом key в таблице.
     *
     * @param key ключ, который нужно найти
     * @return true, если ключ найден, и false, если ключ не найден
     */
    public boolean contains(String key) {
        return table[hash(key, numOfLists)].contains(key);
    }

    /**
     * Возвращает значение по ключу или null, если такого ключа нет.
     *
     * @param key ключ, который нужно найти
     * @return значение по ключу или null, если такого ключа нет
     */
    public String get(String key) {
        return table[hash(key, numOfLists)].get(key);
    }

    /**
     * Кладет в хеш-таблицу значение value по ключу key.
     * В случае, если в таблице много элементов (в 16 раз больше, чем листов),
     * увеличивает количество листов перед тем, как добавить новый элемент.
     *
     * @param key ключ, по которому кладется значение
     * @param value значение
     * @return значение, которое лежало по ключу или null, если такого ключа не было
     */
    public String put(String key, String value) {
        if (numOfElements > MAX_SIZE_PER_LIST * numOfLists)
            resize(MAX_SIZE_PER_LIST * numOfLists);

        String toReturn = table[hash(key, numOfLists)].put(key, value);
        if (toReturn == null) {
            numOfElements++;
        }

        return toReturn;
    }

    /**
     * Меняет размер массива на заданный и перехэширует все элементы туда.
     *
     * @param newNumOfLists новое количество листов в таблице.
     */
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

    /**
     * Удаляет из хэш-таблицы значение по заданному ключу.
     *
     * @param key ключ, по которому удаляется значение
     * @return удаленное значение, либо null, если ничего не было
     */
    public String remove(String key) {
        String toReturn = table[hash(key, numOfLists)].remove(key);

        if (toReturn != null)
            numOfElements--;

        return toReturn;
    }

    /** Очищает хэш-таблицу. */
    public void clear() {
        table = new MyList[numOfLists];

        for (int i = 0; i < numOfLists; i++) {
            table[i] = new MyList();
        }

        numOfElements = 0;
    }

    /**
     * Функция, вычисляющая хэш строки по модулю переданного числа.
     * @param key строка, хэш которой нужен
     * @param mod число, по модулю которого вычисляется хэш
     * @return хеш строки key по модулю mod
     */
    private int hash(String key, int mod) {
        return abs(key.hashCode()) % mod;
    }

}
