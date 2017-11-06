package ru.spbau.mit.java.paradov;

/**
 * Class that contains unique elements. To get fast access, it uses binary unbalanced tree:
 * all objects greater than object in the vertex are in its right subtree,
 * all objects less - in its left subtree.
 * @param <T> type of elements, that set keeps
 */
public class Set<T extends Comparable<T>> {
    /** Head of a binary tree. */
    private Node head = new Node();

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
        Node current = head;
        while (!current.isLeaf()) {
            if (current.content.compareTo(obj) == 0) {
                return true;
            }

            if (current.content.compareTo(obj) > 0) {
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

        Node current = head;
        while (!current.isLeaf()) {
            current.size++;
            if (current.content.compareTo(obj) > 0) {
                current = current.rightBranch;
            } else {
                current = current.leftBranch;
            }
        }

        current.fill(obj);
    }

    /**
     * Element of binary tree, which set uses.
     */
    private class Node {
        /** Value that node keeps. */
        private T content = null;

        /** Size of subtree of this node. If size is 0, this node considered as leaf. */
        private int size = 0;

        /** Head of left subtree, where all the elements less than this element are kept. */
        private Node leftBranch = null;

        /** Head of right subtree, where all the elements greater than this element are kept. */
        private Node rightBranch = null;

        /**
         * Puts object into Node, creates two empty leaves - children of
         * this node, therefore this node is not a leaf anymore.
         * @param obj object that we put into node.
         */
        private void fill(T obj) {
            content = obj;
            leftBranch = new Node();
            rightBranch = new Node();
            size++;
        }

        /**
         * Checks if this node is a leaf - empty node without content and children.
         * @return true, if this node is a leaf, false otherwise
         */
        private boolean isLeaf() {
            return size == 0;
        }
    }
}
