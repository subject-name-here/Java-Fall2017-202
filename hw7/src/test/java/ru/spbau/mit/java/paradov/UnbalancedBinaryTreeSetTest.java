package ru.spbau.mit.java.paradov;

import org.junit.Test;

import static org.junit.Assert.*;

/** Tests methods of UnbalancedTreeSet. */
public class UnbalancedBinaryTreeSetTest {
    /** Tests if size of empty set is zero. */
    @Test
    public void testSizeOfEmptySetIsZero() {
        assertEquals(0, new UnbalancedBinaryTreeSet<>().size());
    }

    /** Tests if in small set method contains() works as expected. */
    @Test
    public void testContainsWorksCorrectlyInSmallSet() {
        UnbalancedBinaryTreeSet<Integer> set = new UnbalancedBinaryTreeSet<>();
        set.add(2);

        assertTrue(set.contains(2));
        assertFalse(set.contains(3));
        assertFalse(set.contains(1));
        assertFalse(set.contains(0));
    }

    /** Tests if method add() changes size correctly. */
    @Test
    public void testAddIncreasesSizeCorrectly() {
        UnbalancedBinaryTreeSet<Integer> set = new UnbalancedBinaryTreeSet<>();
        set.add(3);
        set.add(0);
        set.add(5);
        set.add(1);
        set.add(4);
        set.add(2);

        assertEquals(6, set.size());
    }

    /** Tests if add of element, that already is in set, doesn't increase size. */
    @Test
    public void testAddCollisionDoesNotIncreaseSize() {
        UnbalancedBinaryTreeSet<Integer> set = new UnbalancedBinaryTreeSet<>();
        set.add(2);
        set.add(3);
        set.add(1);

        set.add(1);
        set.add(2);

        assertEquals(3, set.size());
    }

    /** Tests if multiple adds (sometimes with collision) work as expected. */
    @Test
    public void testMultipleAddsChangeSetCorrectly() {
        UnbalancedBinaryTreeSet<Integer> set = new UnbalancedBinaryTreeSet<>();
        set.add(5);
        set.add(4);
        set.add(4);
        set.add(3);
        set.add(2);
        set.add(5);

        assertEquals(4, set.size());
        for (int i = 2; i <= 5; i++) {
            assertTrue(set.contains(i));
        }

        for (int i = 1; i <= 10; i++) {
            set.add(i);
        }

        assertEquals(10, set.size());
        for (int i = 1; i <= 10; i++) {
            assertTrue(set.contains(i));
        }

        assertFalse(set.contains(0));
        assertFalse(set.contains(-1));
        assertFalse(set.contains(11));
        assertFalse(set.contains(12));
    }

    /** Tests if in small set method remove() works correctly. */
    @Test
    public void testRemoveWorksCorrectlyInSmallSet() {
        UnbalancedBinaryTreeSet<Integer> set = new UnbalancedBinaryTreeSet<>();
        set.add(2);
        set.remove(2);

        assertEquals(0, set.size());
        assertFalse(set.contains(2));

        set.add(2);
        set.add(3);
        set.remove(2);

        assertEquals(1, set.size());
        assertFalse(set.contains(2));
        assertTrue(set.contains(3));
    }

    /** Tests if remove() of element that was not in set works correctly. */
    @Test
    public void testCollisionRemovesWorkCorrectly() {
        UnbalancedBinaryTreeSet<Integer> set = new UnbalancedBinaryTreeSet<>();
        set.add(2);
        set.add(3);
        assertFalse(set.remove(4));

        assertEquals(2, set.size());
        assertFalse(set.contains(4));
        assertTrue(set.contains(2));
        assertTrue(set.contains(3));
    }

    /** Tests if combitation of adds and removes works as expected. */
    @Test
    public void testMultipleAddsAndRemovesWorkCorrectly() {
        UnbalancedBinaryTreeSet<Integer> set = new UnbalancedBinaryTreeSet<>();

        for (int i = -10; i <= 10; i++) {
            set.add(i);
            if (i > 0 && i < 6)
                set.remove(i - 10);
        }

        assertEquals(16, set.size());
        for (int i = -10; i <= 10; i++) {
            if (i != -10 && i <= -5){
                assertFalse(set.contains(i));
            } else {
                assertTrue(set.contains(i));
            }
        }

        for (int i = -20; i <= 0; i++) {
            set.add(i);
            set.remove(i - 1);
        }

        assertEquals(11, set.size());
        for (int i = -20; i <= 22; i++) {
            if (0 <= i && i <= 10){
                assertTrue(set.contains(i));
            } else {
                assertFalse(set.contains(i));
            }
        }
    }

    /** Tests if set with given comparator has right order of elements. */
    @Test
    public void testSetWithComparatorWorksCorrectly() {
        UnbalancedBinaryTreeSet<Integer> set = new UnbalancedBinaryTreeSet<>((t1, t2) -> {
            if (t1 % 2 == 0 && t2 % 2 != 0)
                return -1;
            if (t2 % 2 == 0 && t1 % 2 != 0)
                return 1;

            return t1 - t2;
        });

        for (int i = 0; i < 20; i++) {
            set.add(i);
        }

        assertEquals(0, (int) set.first());
        assertEquals(19, (int) set.last());

        Integer[] expected = {0, 2, 4, 6, 8, 10, 12, 14, 16, 18, 1, 3, 5, 7, 9, 11, 13, 15, 17, 19};
        Integer[] actual = new Integer[20];

        assertArrayEquals(expected, set.toArray(actual));
    }

    /** Tests if after cleaning set there is no elements in it and it's empty. */
    @Test
    public void testClear() {
        UnbalancedBinaryTreeSet<Integer> set = new UnbalancedBinaryTreeSet<>();
        for (int i = 0; i < 100; i++) {
            set.add(i);
        }
        set.clear();

        assertEquals(0, set.size());
        assertArrayEquals(new Integer[0], set.toArray());
        for (int i = 0; i < 100; i++)
            assertFalse(set.contains(i));
    }

    /** Tests that first() and last() work as expected. */
    @Test
    public void testFirstAndLast() {
        UnbalancedBinaryTreeSet<Integer> set = new UnbalancedBinaryTreeSet<>();
        set.add(-2);
        assertEquals(-2, (int) set.first());
        assertEquals(-2, (int) set.last());

        for (int i = 0; i < 10; i++)
            set.add(i);

        assertEquals(-2, (int) set.first());
        assertEquals(9, (int) set.last());

        set.add(10);
        set.add(-10);
        assertEquals(-10, (int) set.first());
        assertEquals(10, (int) set.last());

        for (int i = 0; i <= 10; i++)
            set.remove(i);

        assertEquals(-10, (int) set.first());
        assertEquals(-2, (int) set.last());
    }

    /** Tests that lower() and higher() in small sets work as expected. */
    @Test
    public void testLowerAndHigherInSmallSets() {
        UnbalancedBinaryTreeSet<Integer> set = new UnbalancedBinaryTreeSet<>();
        set.add(-2);
        set.add(-3);
        assertEquals(-2, (int) set.higher(-3));
        assertEquals(-3, (int) set.higher(-5));
        assertEquals(-2, (int) set.lower(0));
        assertEquals(-3, (int) set.lower(-2));

        assertNull(set.higher(0));
        assertNull(set.higher(-2));
        assertNull(set.lower(-5));
        assertNull(set.lower(-3));
    }

    /** Tests that lower() and higher() work as expected. */
    @Test
    public void testLowerAndHigher() {
        UnbalancedBinaryTreeSet<Integer> set = new UnbalancedBinaryTreeSet<>();
        set.add(42);
        assertEquals(42, (int) set.higher(40));
        assertEquals(42, (int) set.lower(44));
        assertNull(set.lower(42));
        assertNull(set.lower(40));
        assertNull(set.higher(44));

        for (int i = 0; i < 20; i++) {
            set.add(i);
            if (i != 0)
                assertEquals(i - 1, (int) set.lower(i));
            else
                assertNull(set.lower(i));
        }

    }

}