package ru.spbau.mit.java.paradov;

import java.util.ArrayList;

import org.junit.Test;
import static org.junit.Assert.*;

/** Class that tests functionality of Predicate class. */
public class PredicateTest {
    /**
     * Tests if Predicate that method ALWAYS_TRUE returns works correctly
     * (always returns true, no matter what we put there).
     */
    @Test
    public void testConstantPredicateTrueWorksCorrectly() {
        Predicate<Integer> classExemplar = arg -> arg % 22229 == 42;

        @SuppressWarnings("unchecked") //this is okay, because I'm sure it's legal
        Predicate<Object> alwaysTrue = (Predicate<Object>) classExemplar.ALWAYS_TRUE();

        assertTrue(alwaysTrue.apply(42));
        assertTrue(alwaysTrue.apply("String"));
        assertTrue(alwaysTrue.apply(null));
        assertTrue(alwaysTrue.apply(new ArrayList<Integer>()));
    }

    /**
     * Tests if Predicate that method ALWAYS_FALSE returns works correctly
     * (always returns false, no matter what we put there).
     */
    @Test
    public void testConstantPredicateFalseWorksCorrectly() {
        Predicate<Integer> classExemplar = arg -> arg % 22229 == 42;

        @SuppressWarnings("unchecked") //this is okay, because I'm sure it's legal
        Predicate<Object> alwaysFalse = (Predicate<Object>) classExemplar.ALWAYS_FALSE();

        assertFalse(alwaysFalse.apply(42));
        assertFalse(alwaysFalse.apply("String"));
        assertFalse(alwaysFalse.apply(null));
        assertFalse(alwaysFalse.apply(new ArrayList<Integer>()));
    }

    /** Tests if Predicate.not() really negates the result of given function. */
    @Test
    public void testNegationOfPredicateWorksCorrectly() {
        Predicate<Integer> isEven = arg -> arg % 2 == 0;
        Predicate<Integer> isOdd = isEven.not();

        assertTrue(isOdd.apply(1));
        assertTrue(isOdd.apply(-1));
        assertTrue(isOdd.apply(3));
        assertTrue(isOdd.apply(528491));

        assertFalse(isOdd.apply(0));
        assertFalse(isOdd.apply(2));
        assertFalse(isOdd.apply(18));
        assertFalse(isOdd.apply(65536));
    }

    /** Tests if disjunction of two predicates works correctly. */
    @Test
    public void testPredicateDisjunctionWorksCorrectly() {
        Predicate<String> p1 = arg -> arg.length() <= 4;
        Predicate<String> p2 = arg -> arg.length() >= 9;
        Predicate<String> p = p1.or(p2);

        assertFalse(p.apply("coffee"));
        assertFalse(p.apply("fives"));
        assertFalse(p.apply("spagetti"));

        assertTrue(p.apply(""));
        assertTrue(p.apply("mark"));
        assertTrue(p.apply("adrenalin"));
        assertTrue(p.apply("super power"));
    }

    /** Tests if disjunction of two predicates works correctly. */
    @Test
    public void testPredicateConjunctionWorksCorrectly() {
        Predicate<String> p1 = arg -> arg.length() >= 4;
        Predicate<String> p2 = arg -> arg.length() <= 9;
        Predicate<String> p = p1.and(p2);

        assertTrue(p.apply("coffee"));
        assertTrue(p.apply("true"));
        assertTrue(p.apply("adventure"));

        assertFalse(p.apply(""));
        assertFalse(p.apply("cat"));
        assertFalse(p.apply("wilderness"));
        assertFalse(p.apply("super power"));
    }

    /** Tests if disjunction and conjunction of predicates are lazy. */
    @Test
    public void testPredicateDisjunctionAndConjunctionAreLazy() {
        //call of this predicate will throw an error
        Predicate<String> pDangerous = arg -> null;

        Predicate<String> p = arg -> arg.charAt(1) == 'a';

        assertTrue(p.or(pDangerous).apply("cat"));
        assertTrue(p.or(pDangerous).apply("fall"));

        assertFalse(p.and(pDangerous).apply("dog"));
        assertFalse(p.and(pDangerous).apply("don't fall"));
    }



}