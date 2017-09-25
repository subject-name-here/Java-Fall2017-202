package ru.spbau.mit.java.paradov;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MyListTest {
    /**
     * Tests if getHead() used on empty list returns null.
     */
    @Test
    void testGetHeadWithEmptyList() {
        MyList l = new MyList();
        assertEquals(null, l.getHead());
    }

    /**
     * Tests if put() inserts value in list.
     */
    @Test
    void testPutInsertsValue() {
        MyList l = new MyList();
        l.put("key", "value");

        assertEquals(true, l.contains("key"));
    }

    @Test
    void testGetReturnsRightValue() {
        MyList l = new MyList();
        l.put("key", "value");

        assertEquals("value", l.get("key"));
    }

    /**
     * Tests if put() returns null when element was not in list.
     */
    @Test
    void testPutReturnsNull() {
        MyList l = new MyList();
        l.put("key", "value");

        assertEquals(null, l.put("another key", "another value"));
    }

    /**
     * Tests if put() returns right value when element was in list.
     */
    @Test
    void testPutReturnsRightValue() {
        MyList l = new MyList();
        l.put("key", "value");

        assertEquals("value", l.put("key", "another value"));
    }

    /**
     * Tests if contains() returns false when element is not in list.
     */
    @Test
    void testContainsReturnsFalse() {
        MyList l = new MyList();
        l.put("key", "value");

        assertEquals(false, l.contains("not key"));
    }

    /**
     * Tests if get() returns null when element is not in list.
     */
    @Test
    void testGetReturnsNull() {
        MyList l = new MyList();
        l.put("key", "value");

        assertEquals(null, l.get("not key"));
    }

    /**
     * Tests if remove() really deletes element.
     */
    @Test
    void testRemoveDeletesElement() {
        MyList l = new MyList();
        l.put("key", "value");
        l.remove("key");

        assertEquals(false, l.contains("key"));
    }

    /**
     * Tests if remove() returns right value when deletes element.
     */
    @Test
    void testRemoveReturnsRightValue() {
        MyList l = new MyList();
        l.put("key", "value");

        assertEquals("value", l.remove("key"));
    }

    /**
     * Tests if remove() returns null when deletes element that is not in list.
     */
    @Test
    void testRemoveReturnsNull() {
        MyList l = new MyList();
        l.put("key", "value");

        assertEquals(null, l.remove("another key"));
    }

    /**
     * Tests if after multiple insertions put() returns right value when element was in list.
     */
    @Test
    void testPutReturnsRightValueAfterMultiplePuts() {
        MyList l = new MyList();
        for (int i = 0; i < 10; i++)
            l.put("key" + i, "value" + i);

        assertEquals("value5", l.put("key5", "another value"));
    }

    /**
     * Tests if after multiple insertions get() returns right value when element was in list.
     */
    @Test
    void testGetReturnsRightValueAfterMultiplePuts() {
        MyList l = new MyList();
        for (int i = 0; i < 10; i++)
            l.put("key" + i, "value" + i);

        assertEquals("value4", l.get("key4"));
    }

    /**
     * Tests if after multiple insertions remove() deletes right element.
     */
    @Test
    void testRemoveDeletesElementAfterMultiplePuts() {
        MyList l = new MyList();
        for (int i = 0; i < 10; i++)
            l.put("key" + i, "value" + i);
        l.remove("key5");

        assertEquals(false, l.contains("key5"));
    }

    /**
     * Tests if after multiple insertions remove() deletes head correctly.
     */
    @Test
    void testRemoveDeletesHeadAfterMultiplePuts() {
        MyList l = new MyList();
        for (int i = 0; i < 10; i++)
            l.put("key" + i, "value" + i);
        String oldHeadKey = l.getHeadKey();
        l.remove(oldHeadKey);

        assertEquals(false, l.contains(oldHeadKey));
    }

    /**
     * Tests if after multiple insertions and remove() of head new head is correct.
     * Don't forget that elements are putting in list as in stack,
     * so the last element that we put is head.
     */
    @Test
    void testHeadIsCorrectAfterMultiplePutsAndRemoveHead() {
        MyList l = new MyList();
        for (int i = 0; i < 10; i++)
            l.put("key" + i, "value" + i);
        l.remove(l.getHeadKey());

        assertEquals("key8", l.getHeadKey());
    }

}