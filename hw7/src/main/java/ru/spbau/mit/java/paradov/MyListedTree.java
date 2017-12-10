package ru.spbau.mit.java.paradov;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Class used to store data in UnbalancedBinaryTreeSet. Basically, it's an unbalanced
 * binary tree, but all elements are also forming the structure of linked list.
 */
public class MyListedTree<E> {
    /** Comparator used to compare elements in tree. */
    private Comparator<? super E> cmp;

    /** Root of this tree. */
    private Node root;

    /** Node that contains the lowest value. */
    private Node first;
    /** Node that contains the highest value. */
    private Node last;

    /** Number of elements in tree. */
    private int size = 0;

    /** Constructor from comparator, that will be used to order elements. */
    public MyListedTree (@NotNull Comparator<? super E> comparator) {
        cmp = comparator;
        root = new Node();
        first = last = root;
    }

    public Comparator<? super E> getComparator() {
        return cmp;
    }

    public int size() {
        return size;
    }

    public E first() {
        return first.value;
    }

    public E last() {
        return last.value;
    }


    /**
     * Finds the greatest element in tree less than given.
     * @param e given element
     * @return the greatest element less than given, or null, if there is no such element
     */
    public E lessThan(E e) {
        return root.lessThan(e);
    }

    /**
     * Finds the least element in tree greater than given.
     * @param e given element
     * @return the least element greater than given, or null, if there is no such element
     */
    public E greaterThan(E e) {
        return root.greaterThan(e);
    }

    /**
     * Checks if given object is in tree.
     * @param o object to find
     * @return true, if object is in tree, false otherwise
     */
    public boolean contains(Object o) {
        return root.containsInSubtree((E) o);
    }

    /**
     * Adds given value to tree; if it is already here, does nothing and returns false.
     * @param e value to add
     * @return true, if value was added in tree, false, if value already was in tree
     */
    public boolean add(E e) {
        if (size == 0) {
            root = new Node(e, null);
            first = last = root;
        } else {
            Node newNode = root.addInSubtree(e);
            if (newNode == null)
                return false;

            E next = root.greaterThan(e);
            if (next == null) {
                insertNewNext(last, newNode);
                last = newNode;
            } else {
                Node nextNode = root.findNode(next);
                insertNewPrev(nextNode, newNode);

                if (cmp.compare(first.value, e) > 0) {
                    first = newNode;
                }
            }
        }

        size++;
        return true;
    }

    /**
     * Removes value from tree; if there was no such value, returns false.
     * @param o object to remove
     * @return true, if object was successfully removed, false otherwise
     */
    public boolean remove(Object o) {
        if (!contains(o))
            return false;

        if (size == 1) {
            root = new Node();
            first = last = root;
        } else {
            E nextValue = root.greaterThan((E) o);
            Node nextNode, thisNode;
            if (nextValue == null) {
                thisNode = last;
                last = thisNode.prevNode;
            } else {
                nextNode = root.findNode(nextValue);
                thisNode = nextNode.prevNode;
                if (cmp.compare(first.value, thisNode.value) == 0) {
                    first = thisNode.nextNode;
                }
            }

            Node newRootOfSubtree = thisNode.removeNodeFromItsSubtree();
            deleteFromList(thisNode);

            if (thisNode == root) {
                root = newRootOfSubtree;
            }
        }

        size--;
        return true;
    }

    /**
     * Inserts given node in list structure after this node.
     * @param currentNode node after which we insert
     * @param newNext node to insert
     */
    private void insertNewNext(Node currentNode, Node newNext) {
        Node oldNext = currentNode.nextNode;
        currentNode.nextNode = newNext;

        if (oldNext != null)
            oldNext.prevNode = newNext;

        newNext.prevNode = currentNode;
        newNext.nextNode = oldNext;
    }

    /**
     * Inserts new node in list structure before current node.
     * @param currentNode node before which we insert
     * @param newPrev node to insert
     */
    private void insertNewPrev(Node currentNode, Node newPrev) {
        Node oldPrev = currentNode.prevNode;
        currentNode.prevNode = newPrev;

        if (oldPrev != null)
            oldPrev.nextNode = newPrev;

        newPrev.prevNode = oldPrev;
        newPrev.nextNode = currentNode;
    }

    /**
     * Removes node from list structure.
     * @param currentNode node which we remove
     */
    private void deleteFromList(Node currentNode) {
        if (currentNode.nextNode != null) {
            currentNode.nextNode.prevNode = currentNode.prevNode;
        }
        if (currentNode.prevNode != null) {
            currentNode.prevNode.nextNode = currentNode.nextNode;
        }
    }

    /**
     * Returns iterator over the list structure of tree.
     * @param isReversed flag: true, if iterator is on reversed list, false otherwise
     * @return iterator in specified by given flag order
     */
    public MyListedTreeIterator iterator(boolean isReversed) {
        return new MyListedTreeIterator(isReversed);
    }

    /**
     * Elements that contain data and form a tree. Also, it has structure of
     * ordered list, but all list operations are done in MyListedTree.
     */
    private class Node {
        /** Parent of this node in tree. Parent of root is null. */
        private Node parent;
        /**
         * Left son of this node. All elements in subtree with value lower than
         * value of this node are kept.
         */
        private Node left;
        /**
         * Right son of this node. All elements in subtree with value higher than
         * value of this node are kept.
         */
        private Node right;

        /** Node next to this one in ordered list. If this node is last, nextNode is null. */
        private Node nextNode;
        /** Node before this one in ordered list. If this node is first, prevNode is null. */
        private Node prevNode;

        /** Value that this node keeps. */
        private E value;

        /**
         * Standard constructor for Node. Creates empty node.
         */
        private Node() {
            this.value = null;
            this.parent = null;
        }

        /**
         * Constructor for Node. Creates Node with given value.
         * @param value value to keep in new Node
         * @param parent parent of new node in tree
         */
        private Node(E value, Node parent) {
            this.value = value;
            this.parent = parent;
        }

        /**
         * Checks if there is a node in subtree of this node with given value.
         * @param that value to check
         * @return true, if there is a node with value equals to given one, false otherwise
         */
        private boolean containsInSubtree(E that) {
            return value != null && findNode(that) != null;
        }

        /**
         * Finds in tree node with given value.
         * @param that value to find
         * @return node with given value, or null if there is no such node
         */
        private Node findNode(E that) {
            if (this.value == null)
                return null;

            int cmpResult = cmp.compare(value, that);

            if (cmpResult == 0) {
                return this;
            } else if (cmpResult > 0 && left != null) {
                return left.findNode(that);
            } else if (cmpResult < 0 && right != null) {
                return right.findNode(that);
            }

            return null;
        }

        /**
         * Returns value of the greatest element in tree strictly less than the given element,
         * or null if there is no such element.
         * @param o given element
         * @return value of the greatest element less than given element, or null,
         * if there is no value less than given one
         */
        private @Nullable E lessThan(E o) {
            if (value == null)
                return null;

            int cmpResult = cmp.compare(value, o);
            if (cmpResult < 0) {
                if (right == null) {
                    return this.value;
                } else {
                    E tempResult = right.lessThan(o);
                    return tempResult == null ? this.value : tempResult;
                }
            } else {
                if (left == null) {
                    return null;
                } else {
                    return left.lessThan(o);
                }
            }
        }

        /**
         * Returns value of the least element in tree strictly greater than the given element,
         * or null if there is no such element.
         * @param o given element
         * @return value of the least element greater than given element, or null,
         * if there is no value greater than given one
         */
        private @Nullable E greaterThan(E o) {
            if (value == null)
                return null;

            int cmpResult = cmp.compare(value, o);
            if (cmpResult > 0) {
                if (left == null) {
                    return this.value;
                } else {
                    E tempResult = left.greaterThan(o);
                    return tempResult == null ? this.value : tempResult;
                }
            } else {
                if (right == null) {
                    return null;
                } else {
                    return right.greaterThan(o);
                }
            }
        }

        /**
         * Recursively adds node with given value in subtree of this node.
         * @param e value to add
         * @return null, if node with given value already exists, created node otherwise
         */
        private @Nullable Node addInSubtree(E e) {
            int cmpResult = cmp.compare(value, e);
            if (cmpResult == 0)
                return null;

            if (cmpResult > 0) {
                if (left == null) {
                    return left = new Node(e, this);
                } else {
                    return left.addInSubtree(e);
                }
            } else {
                if (right == null) {
                    return right = new Node(e, this);
                } else {
                    return right.addInSubtree(e);
                }
            }
        }

        /**
         * Removes node from subtree of this node.
         * @return node - new root of subtree of this node
         */
        private Node removeNodeFromItsSubtree() {
            E newRootOfSubtreeValue = greaterThan(value);
            if (newRootOfSubtreeValue == null) {
                if (left != null)
                    left.parent = parent;

                if (parent != null) {
                    if (parent.left == this) {
                        parent.left = left;
                    } else {
                        parent.right = left;
                    }
                }

                return left;
            }

            Node newRootOfSubtree = findNode(newRootOfSubtreeValue);

            if (newRootOfSubtree != right) {
                newRootOfSubtree.parent.left = newRootOfSubtree.right;

                if (newRootOfSubtree.right != null) {
                    newRootOfSubtree.right.parent = newRootOfSubtree.parent;
                }

                newRootOfSubtree.right = right;
                right.parent = newRootOfSubtree;
            }

            newRootOfSubtree.left = left;
            if (left != null)
                left.parent = newRootOfSubtree;

            newRootOfSubtree.parent = this.parent;
            if (this.parent != null) {
                if (this == this.parent.left) {
                    this.parent.left = newRootOfSubtree;
                } else {
                    this.parent.right = newRootOfSubtree;
                }
            }

            return newRootOfSubtree;
        }
    }


    /**
     * Iterator over MyListedTree class. Iterates in natural order (or in order,
     * given by comparator), or in reverse order, depending on instructions.
     */
    private class MyListedTreeIterator implements Iterator<E> {
        /** Points on node before which iterator stands. */
        private Node current;
        /** Flag which defines order of iterator. */
        boolean isReversed;

        /**
         * Creates an iterator which points before first element in ordered list of nodes.
         */
        private MyListedTreeIterator(boolean isReversed) {
            this.isReversed = isReversed;
            if (!isReversed) {
                current = MyListedTree.this.size == 0 ? null : first;
            } else {
                current = MyListedTree.this.size == 0 ? null : last;
            }
        }

        /**
         * Returns true if the iteration has more elements.
         * @return true if the iteration has more elements, false otherwise
         */
        @Override
        public boolean hasNext() {
            return current != null;
        }

        /**
         * Returns the next element in the iteration.
         * @return the next element in the iteration
         * @throws NoSuchElementException if we call next and there is no elements in iteration
         */
        @Override
        public E next() throws NoSuchElementException {
            if (current == null)
                throw new NoSuchElementException();

            E result = current.value;
            if (!isReversed)
                current = current.nextNode;
            else
                current = current.prevNode;

            return result;
        }

        /**
         * Removes from the underlying collection the last element returned by this iterator.
         * If this method called more than once per call to next(), behaviour is undefined.
         * @throws IllegalStateException if next() was not called yet for this iterator
         */
        @Override
        public void remove() throws IllegalStateException {
            if (!isReversed) {
                if (current.prevNode == null) {
                    throw new IllegalStateException();
                }

                MyListedTree.this.remove(current.prevNode.value);
            } else {
                if (current.nextNode == null) {
                    throw new IllegalStateException();
                }

                MyListedTree.this.remove(current.nextNode.value);
            }
        }
    }

}
