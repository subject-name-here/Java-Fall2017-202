package ru.spbau.mit.java.paradov;

/**
 * Class that contains unique elements. To get fast access, it uses binary unbalanced tree with
 * objects hashcode as a key.
 * @param <T> type of elements, that set keeps
 */
public class Set<T> {
    /** Head of a binary tree. */
    private Node<T> head = new Node<>();

    /**
     * Gets number of elements in set.
     * @return size of set
     */
    public int size() {
        return head.size;
    }

    /**
     * Checks if object is in set.
     * @param obj object that we try to find in set
     * @return true, if object was found, false otherwise
     */
    public boolean contains(T obj) {
        Node<T> current = head;
        while (!current.isLeaf()) {
            if (obj.hashCode() == current.content.hashCode()) {
                if (obj.equals(current.content)) {
                    return true;
                }
            }

            if (obj.hashCode() > current.content.hashCode()) {
                current = current.rightBranch;
            } else {
                current = current.leftBranch;
            }
        }

        return false;
    }

    /**
     * If object already is in set, does nothing, otherwise adds it.
     * @param obj object that we want to add
     */
    public void add(T obj) {
        if (contains(obj)) {
            return;
        }

        Node<T> current = head;
        while (!current.isLeaf()) {
            current.size++;
            if (obj.hashCode() > current.content.hashCode()) {
                current = current.rightBranch;
            } else {
                current = current.leftBranch;
            }
        }

        current.fill(obj);
    }

    /**
     * Element of binary tree, which set uses.
     * @param <U> object type, which node keeps
     */
    private class Node<U> {
        /** Value that node keeps. */
        private U content = null;

        /** Size of subtree of this node. */
        private int size = 0;

        /**
         * Head of left subtree, where all the elements with hashcode,
         * greater than hashcode of this node's content, are kept.
         */
        private Node<U> leftBranch = null;

        /**
         * Head of right subtree, where all the elements with hashcode,
         * less or equal than hashcode of this node's content, are kept.
         */
        private Node<U> rightBranch = null;

        /**
         * Puts object into Node, creates two empty leaves - children of
         * this node, therefore this node is not a leaf anymore.
         * @param obj object that we put into node.
         */
        private void fill(U obj) {
            content = obj;
            leftBranch = new Node<>();
            rightBranch = new Node<>();
            size++;
        }

        /**
         * Checks if this node is a leaf - empty node without content.
         * @return true, if this node is a leaf, false otherwise
         */
        private boolean isLeaf() {
            return size == 0;
        }
    }
}
