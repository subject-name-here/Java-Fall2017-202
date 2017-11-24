package ru.spbau.mit.java.paradov;

import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;
import static ru.spbau.mit.java.paradov.Collections.*;

/** Class that tests functionality of methods of Collections class. */
public class CollectionsTest {
    /** Tests if map() works as expected. */
    @Test
    public void testMap() {
        Function1<Integer, String> tripledIntToStr = arg -> {
            Integer new_arg = arg * 3;
            return new_arg.toString();
        };

        ArrayList<Integer> container = new ArrayList<>();
        container.add(4);
        container.add(8);
        container.add(15);
        container.add(16);
        container.add(23);
        container.add(42);
        ArrayList<String> actual = (ArrayList<String>) map(tripledIntToStr, container);

        ArrayList<String> expected = new ArrayList<>();
        expected.add("12");
        expected.add("24");
        expected.add("45");
        expected.add("48");
        expected.add("69");
        expected.add("126");

        assertArrayEquals(expected.toArray(), actual.toArray());
    }

    /** Tests if filter() works as expected. */
    @Test
    public void testFilter() {
        Predicate<String> p = arg -> arg.length() > 5;

        ArrayList<String> data = new ArrayList<>();
        data.add("");
        data.add("map");
        data.add("filter");
        data.add("takeWhile");
        data.add("takeUnless");
        data.add("foldl");
        data.add("foldr");

        ArrayList<String> actual = (ArrayList<String>) filter(p, data);

        ArrayList<String> expected = new ArrayList<>();
        expected.add("filter");
        expected.add("takeWhile");
        expected.add("takeUnless");

        assertArrayEquals(expected.toArray(), actual.toArray());
    }

    /** Tests if takeWhile() works as expected. */
    @Test
    public void testTakeWhile() {
        Predicate<String> p = arg -> arg.length() != 0;

        ArrayList<String> data = new ArrayList<>();
        data.add("map");
        data.add("filter");
        data.add("takeWhile");
        data.add("");
        data.add("takeUnless");
        data.add("foldl");
        data.add("foldr");
        data.add("");

        ArrayList<String> actual1 = (ArrayList<String>) takeWhile(p, data);

        ArrayList<String> expected = new ArrayList<>();
        expected.add("map");
        expected.add("filter");
        expected.add("takeWhile");

        assertArrayEquals(expected.toArray(), actual1.toArray());

        ArrayList<String> actual2 = (ArrayList<String>) takeWhile(p.not(), data);
        assertArrayEquals((new ArrayList<String>()).toArray(), actual2.toArray());
    }

    /** Tests if takeUnless() works as expected. */
    @Test
    public void testTakeUnless() {
        Predicate<String> p = arg -> arg.length() == 0;

        ArrayList<String> data = new ArrayList<>();
        data.add("map");
        data.add("filter");
        data.add("takeWhile");
        data.add("");
        data.add("takeUnless");
        data.add("foldl");
        data.add("foldr");
        data.add("");

        ArrayList<String> actual1 = (ArrayList<String>) takeUnless(p, data);

        ArrayList<String> expected = new ArrayList<>();
        expected.add("map");
        expected.add("filter");
        expected.add("takeWhile");

        assertArrayEquals(expected.toArray(), actual1.toArray());

        ArrayList<String> actual2 = (ArrayList<String>)takeUnless(p.not(), data);
        assertArrayEquals((new ArrayList<String>()).toArray(), actual2.toArray());
    }

    /** Tests if foldl() works as expected. */
    @Test
    public void testFoldl() {
        Function2 <Integer, Boolean, Integer> f = new Function2<Integer, Boolean, Integer>() {
            @Override
            public Integer apply(Integer arg1, Boolean arg2) {
                return arg2 ? arg1 + 1 : arg1 - 1;
            }
        };

        ArrayList<Boolean> a1 = new ArrayList<>();
        ArrayList<Boolean> a2 = new ArrayList<>();
        a2.add(true);
        a2.add(false);
        a2.add(false);
        a2.add(true);
        a2.add(true);
        a2.add(false);
        a2.add(false);
        a2.add(false);
        Integer ini = 0;
        Integer result1 = foldl(f, ini, a1);
        Integer result2 = foldl(f, ini, a2);
        assertEquals(0, (long) result1);
        assertEquals(-2, (long) result2);
    }

    /** Tests if foldr() works as expected. */
    @Test
    public void testFoldr() {
        Function2 <Boolean, Integer, Integer> f = new Function2<Boolean, Integer, Integer>() {
            @Override
            public Integer apply(Boolean arg1, Integer arg2) {
                return arg1 ? arg2 + 1 : arg2 - 1;
            }
        };

        ArrayList<Boolean> a1 = new ArrayList<>();
        ArrayList<Boolean> a2 = new ArrayList<>();
        a2.add(true);
        a2.add(false);
        a2.add(false);
        a2.add(true);
        a2.add(true);
        a2.add(false);
        a2.add(false);
        a2.add(false);
        Integer ini = 0;
        Integer result1 = foldr(f, ini, a1);
        Integer result2 = foldr(f, ini, a2);
        assertEquals(0, (long) result1);
        assertEquals(-2, (long) result2);
    }

}