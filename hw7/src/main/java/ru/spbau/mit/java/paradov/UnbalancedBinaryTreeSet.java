package ru.spbau.mit.java.paradov;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Comparator;
import java.util.Iterator;
import java.util.AbstractSet;
import java.util.NoSuchElementException;

/**
 * Implementation of MyTreeSet, based on AbstractSet. The elements are ordered using their
 * natural ordering, or by a Comparator provided at set creation time, depending on
 * which constructor is used. Because this implementation uses unbalanced binary search tree
 * as a data store, time cost for basic operations (add, remove, contains) can reach O(n).
 * @param <E> type of objects we keep in set
 */
public class UnbalancedBinaryTreeSet<E> extends AbstractSet<E> implements MyTreeSet<E> {
    /** Number of elements in set. */
    private int size = 0;

    /** Comparator used to compare elements in tree. */
    private Comparator<? super E> cmp;

    /** Root of tree, in which we keep all elements. */
    private Node root;

    /** Node that contains the lowest value. */
    private Node first;
    /** Node that contains the highest value. */
    private Node last;

    /**
     * Standard constructor of this class. Assigning to a comparator a standard version:
     * it just assumes that elements of set are Comparable and compares them.
     * If they are not, it will throw ClassCastException.
     */
    public UnbalancedBinaryTreeSet() {
        cmp = (t1, t2) -> ((Comparable) t1).compareTo(t2);
    }

    /**
     * Constructs a new, empty tree set, sorted according to the specified comparator.
     * @param comparator the comparator that will be used to order this set
     */
    public UnbalancedBinaryTreeSet(@NotNull Comparator<? super E> comparator) {
        cmp = comparator;
    }

    /**
     * Returns an iterator over the elements in this set in descending order.
     * @return an iterator over the elements in this set in descending order
     */
    @Override
    public Iterator<E> descendingIterator() {
        return new UnbalancedBinaryDescendingTreeSetIterator();
    }

    /**
     * Returns a reverse order view of the elements contained in this set.
     * The descending set is backed by this set, so changes to the set are reflected
     * in the descending set, and vice-versa.
     * @return a reverse order view of this set
     */
    @Override
    public MyTreeSet<E> descendingSet() {
        return new UnbalancedBinaryDescendingTreeSet(this);
    }

    /**
     * Returns the first (lowest) element currently in this set.
     * @return the first element currently in this set
     * @throws NoSuchElementException if called on empty set
     */
    @Override
    public E first() throws NoSuchElementException {
        if (size == 0)
            throw new NoSuchElementException();

        return first.value;
    }

    /**
     * Returns the last (highest) element currently in this set.
     * @return the last element in this set
     * @throws NoSuchElementException if called on empty set
     */
    @Override
    public E last() throws NoSuchElementException {
        if (size == 0)
            throw new NoSuchElementException();

        return last.value;
    }

    /**
     * Returns the greatest element in this set strictly less than the given element,
     * or null if there is no such element.
     * @param e value to match
     * @return the greatest element less than e, or null if there is no such element
     */
    @Override
    public E lower(E e) {
        if (size == 0)
            return null;

        Node result = root.lessThan(e);

        return result != null ? result.value : null;
    }

    /**
     * Returns the greatest element in this set less than or equal to the given element,
     * or null if there is no such element.
     * @param e value to match
     * @return the greatest element not greater than e, or null if there is no such element
     */
    @Override
    public E floor(E e) {
        return contains(e) ? e : lower(e);
    }

    /**
     * Returns the least element in this set greater than or equal to the given element,
     * or null if there is no such element.
     * @param e value to match
     * @return the least element not less e, or null if there is no such element
     */
    @Override
    public E ceiling(E e) {
        return contains(e) ? e : higher(e);
    }

    /**
     * Returns the least element in this set strictly greater than the given element,
     * or null if there is no such element.
     * @param e value to match
     * @return the least element greater than e, or null if there is no such element
     */
    @Override
    public E higher(E e) {
        if (size == 0)
            return null;

        Node result = root.greaterThan(e);

        return result != null ? result.value : null;
    }

    /**
     * Returns number of elements in this set.
     * @return number of elements in this set
     */
    @Override
    public int size() {
        return size;
    }

    /**
     * Returns true if this set contains the specified element, false otherwise.
     * @param o object to be checked for containment in this set
     * @return true if this set contains the specified element
     */
    @Override
    public boolean contains(Object o) {
        return (size != 0) && root.containsInSubtree((E) o);
    }

    /**
     * Returns iterator over this set in ascending order.
     * @return iterator over this set in ascending order
     */
    @Override
    public @NotNull Iterator<E> iterator() {
        return new UnbalancedBinaryTreeSetIterator();
    }

    /**
     * Adds element to this set if it is not already present. If element is in list,
     * it does nothing and returns false.
     * @param e value to add
     * @return true, if element was added, false, if element already is in list
     */
    @Override
    public boolean add(E e) {
        if (size == 0) {
            root = new Node(e, null);
            first = last = root;
        } else {
            Node newNode = root.addInSubtree(e);
            if (newNode == null)
                return false;

            Node next = root.greaterThan(e);
            if (next == null) {
                last.insertNewNext(newNode);
                last = newNode;
            } else {
                next.insertNewPrev(newNode);

                if (cmp.compare(first.value, e) > 0) {
                    first = newNode;
                }
            }
        }

        size++;
        return true;
    }

    /**
     * Removes the specified element from this set if it is present. Returns true, if set
     * contained this element.
     * @param o object we want to remove from set
     * @return true, if object was in set and now deleted, false, if object was not in set
     */
    @Override
    public boolean remove(Object o) {
        if (!contains(o))
            return false;

        Node nextNode = root.greaterThan((E) o);
        Node thisNode;
        if (nextNode == null) {
            thisNode = last;
            last = thisNode.prevNode;
        } else {
            thisNode = nextNode.prevNode;
        }

        if (cmp.compare(first.value, thisNode.value) == 0) {
            first = thisNode.nextNode;
        }

        Node newRootOfSubtree = thisNode.removeNodeFromItsSubtree();
        thisNode.deleteFromList();
        size--;

        if (thisNode.parent == null) {
            root = newRootOfSubtree;
        } else {
            if (thisNode.parent.left == thisNode) {
                thisNode.parent.left = newRootOfSubtree;
            } else {
                thisNode.parent.right = newRootOfSubtree;
            }
        }

        if (newRootOfSubtree != null)
            newRootOfSubtree.parent = thisNode.parent;

        return true;
    }

    /**
     * Removes all of the elements from this set. The set will be empty after this call returns.
     */
    @Override
    public void clear() {
        root = null;
        size = 0;
        first = null;
        last = null;
    }

    /**
     * Elements of set that contain data and form a tree. Also, it has structure of
     * ordered list, head and tail of which are contained as set's fields.
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
            int cmpResult = cmp.compare(value, that);

            if (cmpResult > 0) {
                return left != null && left.containsInSubtree(that);
            } else
                return cmpResult == 0 || right != null && right.containsInSubtree(that);
        }

        /**
         * Returns value of the greatest element in tree strictly less than the given element,
         * or null if there is no such element.
         * @param o given element
         * @return value of the greatest element less than given element, or null,
         * if there is no value less than given one
         */
        private @Nullable Node lessThan(E o) {
            int cmpResult = cmp.compare(value, o);
            if (cmpResult < 0) {
                if (right == null) {
                    return this;
                } else {
                    Node result = right.lessThan(o);
                    return result == null ? this : result;
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
        private @Nullable Node greaterThan(E o) {
            int cmpResult = cmp.compare(value, o);

            if (cmpResult > 0) {
                if (left == null) {
                    return this;
                } else {
                    Node result = left.greaterThan(o);
                    return result == null ? this : result;
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
         * Inserts given node in list structure after this node.
         * @param newNext node to insert
         */
        private void insertNewNext(Node newNext) {
            Node oldNext = nextNode;
            nextNode = newNext;

            if (oldNext != null)
                oldNext.prevNode = newNext;

            newNext.prevNode = this;
            newNext.nextNode = oldNext;
        }

        /**
         * Inserts given node in list structure before this node.
         * @param newPrev node to insert
         */
        private void insertNewPrev(Node newPrev) {
            Node oldPrev = prevNode;
            prevNode = newPrev;

            if (oldPrev != null)
                oldPrev.nextNode = newPrev;

            newPrev.prevNode = oldPrev;
            newPrev.nextNode = this;
        }

        /**
         * Removes node from subtree of this node.
         * @return node - new root of subtree of this node
         */
        private Node removeNodeFromItsSubtree() {
            Node newRootOfSubtree = greaterThan(value);
            if (newRootOfSubtree == null) {
                return left;
            }

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

            return newRootOfSubtree;
        }

        /**
         * Removes node from list structure.
         */
        private void deleteFromList() {
            if (nextNode != null) {
                nextNode.prevNode = prevNode;
            }
            if (prevNode != null) {
                prevNode.nextNode = nextNode;
            }
        }
    }

    /**
     * Iterator over UnbalancedBinaryTreeSet class. Iterates in natural order (or in order,
     * given by comparator).
     */
    private class UnbalancedBinaryTreeSetIterator implements Iterator<E> {
        /** Points on node before which iterator points. */
        private Node current;

        /**
         * Creates an iterator which points before first element in ordered list of nodes.
         */
        private UnbalancedBinaryTreeSetIterator() {
            current = root == null ? null : first;
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
            current = current.nextNode;

            return result;
        }

        /**
         * Removes from the underlying collection the last element returned by this iterator.
         * If this method called more than once per call to next(), behaviour is undefined.
         * @throws IllegalStateException if next() was not called yet for this iterator
         */
        @Override
        public void remove() throws IllegalStateException {
            if (current.prevNode == null) {
                throw new IllegalStateException();
            }

            UnbalancedBinaryTreeSet.this.remove(current.prevNode.value);
        }
    }

    /**
     * Descending set which is totally connected to set from which we called method
     * descendingSet(). Almost every method is call of the same method of father-set.
     */
    private class UnbalancedBinaryDescendingTreeSet extends UnbalancedBinaryTreeSet<E> {
        /*
         * Link to a TreeSet which contains all data in right order or contains a link to that
         * set. We need it to modify set correctly, so if we deleted root in set-heir,
         * in previous set root will be deleted too.
         */
        private UnbalancedBinaryTreeSet<E> previousSet;

        /**
         * Constructor that creates descending sets. Called only from method descendingSet()
         * of fatherSet.
         * @param fatherSet set that contains either link to its father set,
         *                  in which we can change elements
         */
        private UnbalancedBinaryDescendingTreeSet(@NotNull UnbalancedBinaryTreeSet<E> fatherSet) {
            previousSet = fatherSet;
        }

        /**
         * Returns an iterator over the elements in this set in descending order,
         * or more precisely, an iterator over the elements in father-set in ascending order.
         * @return an iterator over the elements in this set in descending order
         */
        @Override
        public Iterator<E> descendingIterator() {
            return previousSet.iterator();
        }

        /**
         * Returns a reverse order view of the elements contained in this set, or
         * more precisely, returns father-set (because it is equivalent to what we want).
         * @return set with reverse order view of elements
         */
        @Override
        public MyTreeSet<E> descendingSet() {
            return previousSet;
        }

        /**
         * Returns the first (lowest) element currently in this set.
         * @return the first element currently in this set
         */
        @Override
        public E first() {
            return previousSet.last();
        }

        /**
         * Returns the last (highest) element currently in this set.
         * @return the last element currently in this set
         */
        @Override
        public E last() {
            return previousSet.first();
        }

        /**
         * Returns the greatest element in this set strictly less than the given element,
         * or null if there is no such element.
         * @param e value to match
         * @return the greatest element less than e, or null if there is no such element
         */
        @Override
        public E lower(E e) {
            return previousSet.higher(e);
        }

        /**
         * Returns the greatest element in this set less than or equal to the given element,
         * or null if there is no such element.
         * @param e value to match
         * @return the greatest element not greater than e, or null if there is no such element
         */
        @Override
        public E floor(E e) {
            return previousSet.ceiling(e);
        }

        /**
         * Returns the least element in this set greater than or equal to the given element,
         * or null if there is no such element.
         * @param e value to match
         * @return the least element not less e, or null if there is no such element
         */
        @Override
        public E ceiling(E e) {
            return previousSet.floor(e);
        }

        /**
         * Returns the least element in this set strictly greater than the given element,
         * or null if there is no such element.
         * @param e value to match
         * @return the least element greater than e, or null if there is no such element
         */
        @Override
        public E higher(E e) {
            return previousSet.lower(e);
        }

        /**
         * Returns number of elements in this set.
         * @return number of elements in this set
         */
        @Override
        public int size() {
            return previousSet.size();
        }

        /**
         * Returns true if this set contains the specified element, false otherwise.
         * @param o object to be checked for containment in this set
         * @return true if this set contains the specified element
         */
        @Override
        public boolean contains(Object o) {
            return previousSet.contains(o);
        }

        /**
         * Returns an iterator over the elements in this set in ascending order.
         * @return an iterator over the elements in this set in ascending order
         */
        @Override
        public @NotNull Iterator<E> iterator() {
            return previousSet.descendingIterator();
        }

        /**
         * Adds a value to set. Actually, it adds value to father-set.
         * @param e value to add
         * @return true, if value was added, false, if this value already was in set
         */
        @Override
        public boolean add(E e) {
            return previousSet.add(e);
        }

        /**
         * Removes object from set by removing it from father-set.
         * @param o object we want to remove from set
         * @return true, if object was removed, false, if object was not in set
         */
        @Override
        public boolean remove(Object o) {
            return previousSet.remove(o);
        }

        /**
         * Delete all objects from father-set, therefore,
         */
        @Override
        public void clear() {
            previousSet.clear();
        }
    }


    /**
     * Iterator over UnbalancedBinaryTreeSet class. Iterates in natural order (or in order,
     * given by comparator).
     */
    private class UnbalancedBinaryDescendingTreeSetIterator implements Iterator<E> {
        /** Points on node before which iterator points. */
        private Node current;

        /**
         * Creates an iterator which points before first element in ordered list of nodes.
         */
        private UnbalancedBinaryDescendingTreeSetIterator() {
            current = root == null ? null : last;
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
            if (current.nextNode == null) {
                throw new IllegalStateException();
            }

            UnbalancedBinaryTreeSet.this.remove(current.prevNode.value);
        }
    }

}
