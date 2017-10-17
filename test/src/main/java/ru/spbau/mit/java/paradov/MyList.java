package ru.spbau.mit.java.paradov;

public class MyList {
    private Node head = null;

    public boolean contains(Object key) {
        Node cur = head;
        while (cur != null) {
            if (key.equals(cur.key))
                return true;
            cur = cur.next;
        }
        return false;
    }

    public boolean containsValue(Object val) {
        Node cur = head;
        while (cur != null) {
            if (val.equals(cur.value))
                return true;
            cur = cur.next;
        }
        return false;
    }

    public Object get(Object key) {
        Node cur = head;
        while (cur != null) {
            if (key.equals(cur.key))
                return cur.value;
            cur = cur.next;
        }

        return null;
    }

    public Object put(Object key, Object value) {
        Node cur = head;
        while (cur != null) {
            if (key.equals(cur.key)) {
                Object oldVal = cur.value;
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

    public Object remove(Object key) {
        Node cur = head;
        while (cur != null) {
            if (key.equals(cur.key)) {
                Object oldVal = cur.value;

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

    public Object getHeadKey() {
        return head.key;
    }

    public Object getHeadValue() {
        return head.value;
    }

    private class Node {
        private Node next;
        private Node prev;

        private Object key;
        private Object value;

        public Node(Object key, Object value, Node prev, Node next) {
            this.next = next;
            this.prev = prev;
            this.key = key;
            this.value = value;
        }
    }
}
