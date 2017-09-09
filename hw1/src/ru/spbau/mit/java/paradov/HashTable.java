package ru.spbau.mit.java.paradov;

/**
 * Хэш-таблица с закрытой адресацией (на самодельных списках).
 */
public class HashTable {

    /**
     * Создает таблицу с 16 списками.
     */
    public HashTable() {
        sz = 0;
        numOfLists = 16;
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
        return sz;
    }

    /**
     * Проверяет, содержится ли элемент с ключом key в таблице.
     *
     * @param key ключ, который нужно найти
     * @return true, если ключ найден, и false, если ключ не найден
     */
    public boolean contains(String key) {
        return table[key.hashCode() % numOfLists].contains(key);
    }

    /**
     * Возвращает значение по ключу или null, если такого ключа нет.
     *
     * @param key ключ, который нужно найти
     * @return значение по ключу или null, если такого ключа нет
     */
    public String get(String key) {
        return table[key.hashCode() % numOfLists].get(key);
    }

    /**
     * Кладет в хеш-таблицу значение value по ключу key.
     * В случае, если в таблице много элементов (в 16 раз больше, чем листов),
     * увеличивает количество листов перед тем, как добавить новый элемент.
     *
     * @param key   ключ, по которому кладется значение
     * @param value значение
     * @return значение, которое лежало по ключу или null, если такого ключа не было
     */
    public String put(String key, String value) {
        if (sz > 16 * numOfLists)
            resize(numOfLists * 16);

        String toReturn = table[key.hashCode() % numOfLists].put(key, value);
        if (toReturn == null) {
            sz++;
        }

        return toReturn;
    }

    /**
     * Увеличичает количество листов в таблице,
     * чтобы в каждом листе было как можно меньше элементов.
     *
     * @param newNumOfLists новое количество листов в таблице.
     */
    private void resize(int newNumOfLists) {
        MyList[] newTable = new MyList[newNumOfLists];

        for (int i = 0; i < numOfLists; i++) {
            while (table[i].getHead() != null) {
                int newListNum = table[i].getHeadKey().hashCode() % newNumOfLists;
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
        String toReturn = table[key.hashCode() % numOfLists].remove(key);

        if (toReturn != null)
            sz--;

        return toReturn;
    }

    /**
     * Очищает хэш-таблицу.
     */
    public void clear() {
        table = new MyList[numOfLists];

        for (int i = 0; i < numOfLists; i++) {
            table[i] = new MyList();
        }

        sz = 0;
    }

    /**
     * Массив листов, в которых хранятся данные.
     */
    private MyList[] table;

    /**
     * Количество элементов в таблице.
     */
    private int sz;

    /**
     * Количество листов в таблице.
     */
    private int numOfLists;
}
