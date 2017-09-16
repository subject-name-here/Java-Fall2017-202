package ru.spbau.mit.java.paradov;

/** Двусвязный список. */
public class MyList {

    /** Голова двусвязного списка. */
    private Node head = null;

    /**
     * Проверяет, содержится ли элемент с ключом key в списке.
     *
     * @param key ключ, который нужно найти
     * @return true, если ключ найден, и false, если ключ не найден
     */
    public boolean contains(String key) {
        Node cur = head;
        while (cur != null) {
            if (key.equals(cur.key))
                return true;
            cur = cur.next;
        }
        return false;
    }

    /**
     * Возвращает значение по ключу или null, если такого ключа нет.
     *
     * @param key ключ, который нужно найти
     * @return значение по ключу или null, если такого ключа нет
     */
    public String get(String key) {
        Node cur = head;
        while (cur != null) {
            if (key.equals(cur.key))
                return cur.value;
            cur = cur.next;
        }

        return null;
    }

    /**
     * Вставляет в лист значение value по ключу key.
     * Если такого ключа не было, то новый элемент вставляется в голову листа.
     *
     * @param key   ключ, по которому кладется значение
     * @param value значение
     * @return значение, которое лежало по ключу или null, если такого ключа не было
     */
    public String put(String key, String value) {
        Node cur = head;
        while (cur != null) {
            if (key.equals(cur.key)) {
                String oldVal = cur.value;
                cur.value = value;
                return oldVal;
            }
            cur = cur.next;
        }

        Node newNode = new Node(key, value, null, head);
        if (head != null)
            head.prev = newNode;
        head = newNode;

        return null;
    }

    /**
     * Удаляет из листа значение по заданному ключу.
     *
     * @param key ключ, по которому удаляется значение
     * @return удаленное значение, либо null, если ничего не было
     */
    public String remove(String key) {
        Node cur = head;
        while (cur != null) {
            if (key.equals(cur.key)) {
                String oldVal = cur.value;

                if (cur.prev != null)
                    cur.prev.next = cur.next;

                if (cur.next != null)
                    cur.next.prev = cur.prev;

                if (cur == head)
                    head = head.next;

                return oldVal;
            }
            cur = cur.next;
        }

        return null;
    }

    public Node getHead() {
        return head;
    }

    public String getHeadKey() {
        return head.key;
    }

    public String getHeadValue() {
        return head.value;
    }

    /**
     * Элемент двусвязного списка MyList, хранящий строку-ключ и строку-значение,
     * а также предыдущий и следующий элемент списка (или null, если таких нет).
     */
    private class Node {
        private Node next;
        private Node prev;

        private String key;
        private String value;

        public Node(String key, String value, Node prev, Node next) {
            this.next = next;
            this.prev = prev;
            this.key = key;
            this.value = value;
        }
    }
}

