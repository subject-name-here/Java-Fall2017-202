package ru.spbau.mit.java.paradov;

import static org.junit.jupiter.api.Assertions.*;

class HashTableTest {
    @org.junit.jupiter.api.Test
    void testSizeOfEmptyTableIsZero() {
        HashTable h = new HashTable();

        assertEquals(0, h.size());
    }

    /**
     * Tests if put() really puts element into hash-table.
     */
    @org.junit.jupiter.api.Test
    void testPutAddsRightElement() {
        HashTable h = new HashTable();
        h.put("key", "value");

        assertEquals(true, h.contains("key"));
    }

    /**
     * Tests if put() increases size, if key was not used.
     */
    @org.junit.jupiter.api.Test
    void testPutIncreasesSize() {
        HashTable h = new HashTable();
        h.put("key", "value");

        assertEquals(1, h.size());
    }

    /**
     * Tests if put() doesn't increases size, if key was used.
     */
    @org.junit.jupiter.api.Test
    void testPutDoesNotIncreasesSize() {
        HashTable h = new HashTable();
        h.put("key", "value");
        h.put("key", "newValue");

        assertEquals(1, h.size());
    }

    /**
     * Tests if contains() returns false when element is not in hash-table.
     */
    @org.junit.jupiter.api.Test
    void testContainsReturnsFalse() {
        HashTable h = new HashTable();
        h.put("key", "value");

        assertEquals(false, h.contains("this key is not in table"));
    }

    /**
     * Tests if get() returns right value when element is in hash-table.
     */
    @org.junit.jupiter.api.Test
    void testGetReturnsRightValue() {
        HashTable h = new HashTable();
        h.put("key", "value");

        assertEquals("value", h.get("key"));
    }

    /**
     * Tests if get() returns null when argument is not in hash-table.
     */
    @org.junit.jupiter.api.Test
    void testGetReturnsNull() {
        HashTable h = new HashTable();
        h.put("key", "value");

        assertEquals(null, h.get("this key is not in table"));
    }

    /**
     * Tests size() after 10 insertions.
     */
    @org.junit.jupiter.api.Test
    void testMultiplePutIncreasesSize() {
        HashTable h = new HashTable();
        for (int i = 0; i < 10; i++){
            h.put("key" + i, "value" + i);
        }

        assertEquals(10, h.size());
    }

    /**
     * Tests if get() returns right value from table with 10 elements.
     */
    @org.junit.jupiter.api.Test
    void testGetAfterMultiplePutReturnsRightValue() {
        HashTable h = new HashTable();
        for (int i = 0; i < 10; i++){
            h.put("key" + i, "value" + i);
        }

        assertEquals("value5", h.get("key5"));
    }

    /**
     * Tests if remove() from empty table returns null (and does not fall).
     */
    @org.junit.jupiter.api.Test
    void testRemoveFromEmptyTable() {
        HashTable h = new HashTable();

        assertEquals(null, h.remove("key"));
    }

    /**
     * Tests if remove() returns right value when element is in hash-table.
     */
    @org.junit.jupiter.api.Test
    void testRemoveReturnsRightValue() {
        HashTable h = new HashTable();
        h.put("key", "value");

        assertEquals("value", h.remove("key"));
    }

    /**
     * Tests if remove() returns null when element is not in hash-table.
     */
    @org.junit.jupiter.api.Test
    void testRemoveReturnsNull() {
        HashTable h = new HashTable();
        h.put("key", "value");

        assertEquals(null, h.remove("this key is not in table"));
    }

    /**
     * Tests if remove() decreases size when element is in hash-table.
     */
    @org.junit.jupiter.api.Test
    void testRemoveDecreasesSize() {
        HashTable h = new HashTable();
        h.put("key", "value");
        h.remove("key");

        assertEquals(0, h.size());
    }

    /**
     * Tests if remove() does not decrease size when element is not in hash-table.
     */
    @org.junit.jupiter.api.Test
    void testRemoveDoesNotDecreaseSize() {
        HashTable h = new HashTable();
        h.put("key", "value");
        h.remove("another key");

        assertEquals(1, h.size());
    }

    /**
     * Tests if remove() really deletes element from table with 10 elements.
     */
    @org.junit.jupiter.api.Test
    void testRemoveAfterMultiplePutsDeletesElement() {
        HashTable h = new HashTable();
        for (int i = 0; i < 10; i++){
            h.put("key" + i, "value" + i);
        }
        h.remove("key4");

        assertEquals(false, h.contains("ke4"));
    }

    /**
     * Tests if size after clear() is 0.
     */
    @org.junit.jupiter.api.Test
    void testSizeAfterClear() {
        HashTable h = new HashTable();
        for (int i = 0; i < 10; i++){
            h.put("key" + i, "value" + i);
        }
        h.clear();

        assertEquals(0, h.size());
    }

    /**
     * Tests if size() works correctly with collisions.
     * Keys in this test have the same hashCode() % 16.
     */
    @org.junit.jupiter.api.Test
    void testSizeWithCollisions() {
        HashTable h = new HashTable();
        h.put("kem5", "value1");
        h.put("kel4", "value2");
        h.put("kex0", "value3");
        h.put("key1", "value4");
        h.put("kez2", "value5");


        assertEquals(5, h.size());
    }

    /**
     * Tests if put() returns null if element is in hash-table.
     */
    @org.junit.jupiter.api.Test
    void testPutReturnsNull() {
        HashTable h = new HashTable();


        assertEquals(null, h.put("key", "value"));
    }

    /**
     * Tests put() with key that already used.
     */
    @org.junit.jupiter.api.Test
    void testPutCollision() {
        HashTable h = new HashTable();
        for (int i = 0; i < 10; i++){
            h.put("key" + i, "value" + i);
        }
        h.put("key5", "newValue5");

        assertEquals("newValue5", h.get("key5"));
    }

    /**
     * Tests put() with key that was already used returns right value.
     */
    @org.junit.jupiter.api.Test
    void testPutCollisionReturnsRightValue() {
        HashTable h = new HashTable();
        for (int i = 0; i < 10; i++){
            h.put("key" + i, "value" + i);
        }


        assertEquals("value5", h.put("key5", "newValue5"));
    }

    /**
     * Tests if size() works correctly after resizing hash-table.
     */
    @org.junit.jupiter.api.Test
    void testSizeAfterMultiplePutsWithResize() {
        HashTable h = new HashTable();
        for (int i = 0; i < 1000; i++){
            h.put("key" + i, "value" + i);
        }

        assertEquals(1000, h.size());
    }

    /**
     * Tests if get() works correctly after resizing hash-table.
     */
    @org.junit.jupiter.api.Test
    void testGetAfterMultiplePutsWithResize() {
        HashTable h = new HashTable();
        for (int i = 0; i < 1000; i++){
            h.put("key" + i, "value" + i);
        }

        assertEquals("value513", h.get("key513"));
    }

    /**
     * Tests if remove() works correctly after resizing hash-table.
     */
    @org.junit.jupiter.api.Test
    void testRemoveAfterMultiplePutsWithResize() {
        HashTable h = new HashTable();
        for (int i = 0; i < 1000; i++){
            h.put("key" + i, "value" + i);
        }
        h.remove("key512");

        assertEquals(null, h.get("key512"));
    }


}