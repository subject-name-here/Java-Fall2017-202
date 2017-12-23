package ru.spbau.mit.java.paradov;

import org.junit.Test;

import java.util.Comparator;
import java.util.Iterator;

import static org.junit.Assert.*;

/** Class that contains tests for MyListedTree methods. */
public class MyListedTreeTest {
    /** Tests if size of empty tree is 0. */
    @Test
    public void testSizeOfEmptyTree() {
        MyListedTree<Integer> tree = new MyListedTree<>(Comparator.naturalOrder());
        assertEquals(0, tree.size());
    }

    /** Tests if add increases size correctly. */
    @Test
    public void testAddIncreasesSize() {
        MyListedTree<Integer> tree = new MyListedTree<>(Comparator.naturalOrder());
        for (int i = 0; i < 10; i++) {
            assertTrue(tree.add(i));
            assertEquals(i + 1, tree.size());
        }
    }

    /** Tests if add really adds elements. */
    @Test
    public void testAddWorksCorrectly() {
        MyListedTree<Integer> tree = new MyListedTree<>(Comparator.naturalOrder());
        for (int i = 0; i < 10; i++) {
            tree.add(i);
        }

        for (int i = 0; i < 10; i++) {
            assertTrue(tree.contains(i));
        }
    }

    /** Tests if adding of elements that are already in set doesn't increase size. */
    @Test
    public void testAddCollisionDoesNotChangesSize() {
        MyListedTree<Integer> tree = new MyListedTree<>(Comparator.naturalOrder());
        for (int i = 0; i < 10; i++) {
            tree.add(i);
        }
        assertEquals(10, tree.size());

        for (int i = 0; i < 10; i++) {
            tree.add(i);
        }
        assertEquals(10, tree.size());
    }

    /** Tests if remove works as expected. */
    @Test
    public void testRemoveOnSmallTrees() {
        MyListedTree<Integer> tree = new MyListedTree<>(Comparator.naturalOrder());
        tree.add(2);
        tree.add(3);

        assertTrue(tree.remove(3));
        assertFalse(tree.remove(1));
        assertFalse(tree.remove(3));
        assertEquals(1, tree.size());

        assertTrue(tree.remove(2));
        assertEquals(0, tree.size());
    }

    /** Tests remove on a big trees. */
    @Test
    public void testRemove() {
        MyListedTree<Integer> tree = new MyListedTree<>(Comparator.naturalOrder());

        for (int i = 0; i < 30; i++) {
            tree.add(i);
        }
        for (int i = 99; i >= 60; i--) {
            tree.add(i);
        }
        for (int i = 30; i < 60; i++) {
            tree.add(i);
        }

        for (int i = -100; i < 200; i++) {
            if (i >= 0 && i < 100) {
                assertTrue(tree.remove(i));
                assertEquals(100 - i - 1, tree.size());
            } else {
                assertFalse(tree.remove(i));
            }
        }

        assertEquals(0, tree.size());
    }

    /** Tests if lessThan and greaterThan work as expected. */
    @Test
    public void testLessAndGreaterThan1() {
        MyListedTree<Integer> tree = new MyListedTree<>(Comparator.naturalOrder());

        for (int i = 0; i < 10; i++) {
            tree.add(i);
        }

        assertEquals(9, (int) tree.lessThan(111));
        assertEquals(9, (int) tree.lessThan(11));
        assertEquals(5, (int) tree.lessThan(6));
        assertEquals(0, (int) tree.lessThan(1));
        assertNull(tree.lessThan(0));
        assertNull(tree.lessThan(-12));

        assertEquals(0, (int) tree.greaterThan(-42));
        assertEquals(0, (int) tree.greaterThan(-1));
        assertEquals(6, (int) tree.greaterThan(5));
        assertEquals(9, (int) tree.greaterThan(8));
        assertNull(tree.greaterThan(9));
        assertNull(tree.greaterThan(12));
    }

    /** The same tests as previous, but order of adding is different now.*/
    @Test
    public void testLessAndGreaterThan2() {
        MyListedTree<Integer> tree = new MyListedTree<>(Comparator.naturalOrder());

        for (int i = 3; i < 6; i++) {
            tree.add(i);
        }
        for (int i = 0; i < 3; i++) {
            tree.add(i);
        }
        for (int i = 9; i >= 6; i--) {
            tree.add(i);
        }

        assertEquals(9, (int) tree.lessThan(111));
        assertEquals(9, (int) tree.lessThan(11));
        assertEquals(5, (int) tree.lessThan(6));
        assertEquals(0, (int) tree.lessThan(1));
        assertNull(tree.lessThan(0));
        assertNull(tree.lessThan(-12));

        assertEquals(0, (int) tree.greaterThan(-42));
        assertEquals(0, (int) tree.greaterThan(-1));
        assertEquals(6, (int) tree.greaterThan(5));
        assertEquals(9, (int) tree.greaterThan(8));
        assertNull(tree.greaterThan(9));
        assertNull(tree.greaterThan(12));
    }

    /** Tests if first and last are counted well. */
    @Test
    public void testFirstAndLast() {
        MyListedTree<Integer> tree = new MyListedTree<>(Comparator.naturalOrder());

        assertNull(tree.first());
        assertNull(tree.last());

        for (int i = 0; i < 10; i++) {
            tree.add(i);
            assertEquals(0, (int) tree.first());
            assertEquals(i, (int) tree.last());
        }

        for (int i = 0; i > -10; i--) {
            tree.add(i);
            assertEquals(i, (int) tree.first());
            assertEquals(9, (int) tree.last());
        }

        tree.remove(9);
        tree.remove(-9);
        assertEquals(8, (int) tree.last());
        assertEquals(-8, (int) tree.first());
    }

    /** Tests if iterator walks correctly through tree. */
    @Test
    public void testIterator() throws Exception {
        MyListedTree<Integer> tree = new MyListedTree<>(Comparator.naturalOrder());

        for (int i = 3; i < 6; i++) {
            tree.add(i);
        }
        for (int i = 0; i < 3; i++) {
            tree.add(i);
        }
        for (int i = 9; i >= 6; i--) {
            tree.add(i);
        }

        Iterator<Integer> iterator = tree.iterator(false);

        int cnt = 0;
        while (iterator.hasNext()) {
            assertEquals(cnt++, (int) iterator.next());
        }
    }

    /** Tests if reversed iterator works correctly. */
    @Test
    public void testReversedIterator() throws Exception {
        MyListedTree<Integer> tree = new MyListedTree<>(Comparator.naturalOrder());

        for (int i = 3; i < 6; i++) {
            tree.add(i);
        }
        for (int i = 0; i < 3; i++) {
            tree.add(i);
        }
        for (int i = 9; i >= 6; i--) {
            tree.add(i);
        }

        Iterator<Integer> iterator = tree.iterator(true);

        int cnt = 9;
        while (iterator.hasNext()) {
            assertEquals(cnt--, (int) iterator.next());
        }
    }

    /** Tests if custom comparator works correctly. */
    @Test
    public void testCustomComparator() throws Exception {
        MyListedTree<String> tree = new MyListedTree<>(Comparator.comparing(String::length));
        assertTrue(tree.add("everything"));
        assertTrue(tree.add("is"));
        assertTrue(tree.add("awesome!"));
        assertFalse(tree.add("Everything"));
        assertFalse(tree.add("is"));
        assertTrue(tree.add("connected"));

        assertEquals(4, tree.size());
        assertTrue(tree.contains("but how?"));

        assertTrue(tree.remove("3v3rY7H1Ng"));

        assertEquals("is", tree.first());
        assertEquals("connected", tree.last());
    }
}