package ru.spbau.mit.java.paradov;

import org.junit.Test;

import static org.junit.Assert.*;

/** Class that tests functionality of class Set and its methods. */
public class SetTest {
    /** Tests if size of empty set is zero. */
    @Test
    public void testSizeOfEmptySetIsZero() {
        Set<Integer> si = new Set<>();
        assertEquals(0, si.size());
    }

    /** Tests if contains() returns false, when there is no elements in set. */
    @Test
    public void testContainsReturnsFalseOnEmptySet() {
        Set<Integer> si = new Set<>();

        assertFalse(si.contains(2));
    }

    /** Tests if after add() set contains element we added. */
    @Test
    public void testAddReallyAddsElement() {
        Set<Integer> si = new Set<>();
        si.add(2);

        assertTrue(si.contains(2));
    }

    /** Tests if contains() returns false, when element is not in set. */
    @Test
    public void testContainsReturnsFalse() {
        Set<Integer> si = new Set<>();
        si.add(2);
        si.add(1);

        assertFalse(si.contains(3));
    }

    /** Tests if a single add() increases size of set to 1. */
    @Test
    public void testAddIncreasesSize() {
        Set<Integer> si = new Set<>();
        si.add(2);

        assertEquals(1, si.size());
    }

    /** Tests if multiple calls of add() change size correctly. */
    @Test
    public void testMultipleAddIncreasesSize() {
        Set<Integer> si = new Set<>();
        si.add(2);
        si.add(3);
        si.add(4);
        si.add(5);

        assertEquals(4, si.size());
    }

    /**
     * Tests if add doesn't increase size,
     * if we tried to add element that already was in set.
     */
    @Test
    public void testAddDoesNotIncreasesSize() {
        Set<Integer> si = new Set<>();
        si.add(1);
        si.add(2);
        si.add(2);

        assertEquals(2, si.size());
    }

    /**
     * Tests if add doesn't drop element (for some reason),
     * if we tried to add the same element.
     */
    @Test
    public void testAddCollisionDoesNotDropElement() {
        Set<Integer> si = new Set<>();
        si.add(2);
        si.add(2);

        assertTrue(si.contains(2));
    }

    /**
     * Tests if multiple calls of add() change the size correctly,
     * even if collisions of objects happen.
     */
    @Test
    public void testMultipleAddAndCollisionAddIncreaseSizeCorrectly() {
        Set<String> si = new Set<>();
        si.add("aac");
        si.add("aAa");
        si.add("Aab");
        si.add("aAa"); //this string was added already
        si.add("aad");
        si.add("aba");
        si.add("Aab"); //this string was added too
        si.add("bab");

        si.add("Aa");
        si.add("BB"); // this two strings have the same hashcode 2112

        assertEquals(8, si.size());
    }

    /**
     * Tests if multiple calls of add() are working correctly (we can find elements later in set),
     * even if collisions of objects happen.
     */
    @Test
    public void testMultipleAddAndCollisionAddWorkCorrectly() {
        Set<Integer> si = new Set<>();
        si.add(9);
        si.add(1);
        si.add(8);
        si.add(2);
        si.add(6);
        si.add(4);
        si.add(1); //this string was added before
        si.add(5);
        si.add(5); //this string was just added
        si.add(9); //this string was the first string we added

        assertTrue(si.contains(2));
        assertTrue(si.contains(8));
        assertTrue(si.contains(5));
        assertTrue(si.contains(1));
        assertTrue(si.contains(9));
        assertTrue(si.contains(4));
        assertTrue(si.contains(6));

        assertFalse(si.contains(3));
        assertFalse(si.contains(7));
    }

}