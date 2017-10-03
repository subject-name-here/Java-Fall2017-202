package ru.spbau.mit.java.paradov;

import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import static org.junit.Assert.*;

/** Tests public methods of class Trie. */
public class TrieTest {
    /** Tests if size of empty trie is 0. */
    @Test
    public void testSizeOfEmptyTrie() {
        Trie t = new Trie();
        assertEquals(0, t.size());
    }

    /** Tests if Add() really adds a string. */
    @Test
    public void testAddString() {
        Trie t = new Trie();
        t.add("string");

        assertEquals(true, t.contains("string"));
    }

    /** Tests if contains() returns false when string is not in trie. */
    @Test
    public void testContainsReturnsFalse() {
        Trie t = new Trie();
        t.add("string");

        assertEquals(false, t.contains("not string"));
    }

    /**
     * Tests if contains() returns false when string is not in trie,
     * and this string is prefix of other string in trie.
     */
    @Test
    public void testContainsReturnsFalseWithStringInTrie() {
        Trie t = new Trie();
        t.add("strings");

        assertEquals(false, t.contains("string"));
    }

    /**
     * Tests if contains() returns false when string is not in trie,
     * and other string in trie is prefix of this string.
     */
    @Test
    public void testContainsReturnsFalseWithStringOutOfTrie() {
        Trie t = new Trie();
        t.add("string");

        assertEquals(false, t.contains("strings"));
    }

    /** Tests if add increases size correctly. */
    @Test
    public void testAddIncreasesSize() {
        Trie t = new Trie();
        t.add("string1");
        t.add("string2");
        t.add("my string");
        t.add("string");

        assertEquals(4, t.size());
    }

    /** Tests if add() returns true when adds element. */
    @Test
    public void testAddReturnsTrue() {
        Trie t = new Trie();
        t.add("string1");
        t.add("string2");

        assertEquals(true, t.add("string3"));
    }

    /** Tests if add() returns false when element already was in trie. */
    @Test
    public void testAddReturnsFalse() {
        Trie t = new Trie();
        t.add("string1");
        t.add("string2");

        assertEquals(false, t.add("string2"));
    }

    /**
     * Tests if add() doesn't increase size when adding element,
     * that already was in trie.
     */
    @Test
    public void testAddDoesNotIncreaseSize() {
        Trie t = new Trie();
        t.add("string1");
        t.add("string2");
        t.add("string2");

        assertEquals(2, t.size());
    }

    /** Tests if remove() really removes element. */
    @Test
    public void testRemoveString() {
        Trie t = new Trie();
        t.add("String1");
        t.add("String2");
        t.remove("String1");

        assertEquals(false, t.contains("String1"));
    }

    /** Tests if remove() decreases size. */
    @Test
    public void testRemoveStringDecreasesSize() {
        Trie t = new Trie();
        t.add("String2");
        t.add("String1");
        t.remove("String2");

        assertEquals(1, t.size());
    }

    /** Tests if remove returns true when deleting element. */
    @Test
    public void testRemoveReturnsTrue() {
        Trie t = new Trie();
        t.add("ASD");
        t.add("SDF");
        t.add("ASDF");

        assertEquals(true, t.remove("ASD"));
    }

    /**
     * Tests if remove returns false, when trying to delete element,
     * that already was in trie.
     */
    @Test
    public void testRemoveReturnsFalse() {
        Trie t = new Trie();
        t.add("QQ");
        t.add("QW");

        assertEquals(false, t.remove("QE"));
    }

    /**
     * Tests if remove doesn't decrease size, when trying to delete element,
     * that already was in trie.
     */
    @Test
    public void testRemoveDoesNotDecreaseSize() {
        Trie t = new Trie();
        t.add("haskell");
        t.add("husky");
        t.add("has");
        t.remove("hask");

        assertEquals(3, t.size());
    }

    /** Tests if combination of remove() and add() changes size correctly. */
    @Test
    public void testRemoveAndAddChangeSizeCorrectly() {
        Trie t = new Trie();
        t.add("four");
        t.add("five");
        t.add("six");
        t.add("one");
        t.remove("six");
        t.remove("seven");
        t.add("two");
        t.add("two");
        t.remove("one");
        t.remove("six");

        assertEquals(3, t.size());
    }

    /**
     * Tests if after add in trie
     * we can decrease its size to 0 with remove().
     */
    @Test
    public void testRemoveAndAddCanMakeSizeZero() {
        Trie t = new Trie();
        t.add("one");
        t.add("two");
        t.add("three");
        t.add("four");
        t.remove("three");
        t.remove("one");
        t.remove("four");
        t.remove("two");

        assertEquals(0, t.size());
    }

    /**
     * Tests if howManyStartsWithPrefix() returns 0,
     * when there are no words starting with prefix.
     */
    @Test
    public void testHowManyStartsWithPrefixReturnsZero() {
        Trie t = new Trie();
        t.add("alt");
        t.add("alp");
        t.add("ald");
        t.add("ali");

        assertEquals(0, t.howManyStartsWithPrefix("all"));
    }

    /**
     * Tests if howManyStartsWithPrefix() returns number of words,
     * starting with prefix.
     */
    @Test
    public void testHowManyStartsWithPrefixReturnsNotZero() {
        Trie t = new Trie();
        t.add("hello");
        t.add("hell");
        t.add("hall");
        t.add("helicopter");
        t.add("hellas");

        assertEquals(3, t.howManyStartsWithPrefix("hell"));
    }

    /** Tests if serialize() and deserialize() don't change size. */
    @Test
    public void testSerializationSavesSize() {
        Trie t = new Trie();
        t.add("one");
        t.add("two");
        t.add("three");
        t.add("infinity");

        byte[] buf;
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            t.serialize(baos);
            buf = baos.toByteArray();
        } catch (Exception e) {
            throw new Error("Something's went wrong.");
        }

        try (ByteArrayInputStream bais = new ByteArrayInputStream(buf)) {
            t.deserialize(bais);
        } catch (Exception e) {
            throw new Error("Something's went wrong.");
        }

        assertEquals(4, t.size());
    }

    /** Tests if serialize() and deserialize() don't save elements. */
    @Test
    public void testSerializationSavesElements() {
        Trie t = new Trie();
        t.add("infinite");
        t.add("infinity");

        byte[] buffer;
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            t.serialize(baos);
            buffer = baos.toByteArray();
        } catch (Exception e) {
            throw new Error("Something's went wrong.");
        }

        try (ByteArrayInputStream bais = new ByteArrayInputStream(buffer)) {
            t.deserialize(bais);
        } catch (Exception e) {
            throw new Error("Something's went wrong.");
        }

        assertEquals(true, t.contains("infinite"));
        assertEquals(true, t.contains("infinity"));
    }

}