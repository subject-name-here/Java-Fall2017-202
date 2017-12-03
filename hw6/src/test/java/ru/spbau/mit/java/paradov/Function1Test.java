package ru.spbau.mit.java.paradov;

import org.junit.Test;

import static org.junit.Assert.*;

/** Class that tests functionality of Function1 class. */
public class Function1Test {
    /** Tests if apply() of a function works as expected. */
    @Test
    public void testApplyWorksCorrectly() {
        Function1<Integer, Integer> f = arg -> arg * 2;
        Integer result = f.apply(21);
        assertEquals(42, (int)result);
    }

    /** Tests if composition of functions works as expected. */
    @Test
    public void testApplyOfCompositionWorksCorrectly() {
        Function1<Integer, String> f1 =
                arg -> arg == 42 ? "Forty-Two" : "Not forty-two";
        Function1<String, Character> f2 =
                arg -> arg.equals("Forty-Two") ? 'T' : 'F';

        Function1<Integer, Character> f = f1.compose(f2);
        assertEquals('T', (long) f.apply(42));
        assertEquals('F', (long) f.apply(43));
    }

    /** Tests if function works even after multiple composition to itself. */
    @Test
    public void testCompositionToItself() {
        Function1<Integer, Integer> f1 = arg -> arg * arg;
        Function1<Integer, Integer> f2 = f1.compose(f1);

        Function1<Integer, Integer> f3 = arg -> arg * 3;
        f3 = f3.compose(f3);

        Function1<Integer, Integer> f4 = arg -> arg + 1;
        for (int i = 0; i < 5; i++) {
            f4 = f4.compose(f4);
        }

        assertEquals(65536, (long) f2.apply(16));
        assertEquals(99, (long) f3.apply(11));
        assertEquals(37, (long) f4.apply(5));
    }

    /** Tests if functions are working with null arguments and can return null. */
    @Test
    public void testApplyWorksCorrectlyWithNullArguments() {
        Function1<Integer, String> f1 = new Function1<Integer, String>() {
            @Override
            public String apply(Integer arg) {
                return arg == null ? "IT'S A NULL!" : "It's not null.";
            }
        };

        Function1<String, Boolean> f2 = new Function1<String, Boolean>() {
            @Override
            public Boolean apply(String arg) {
                return arg.equals("IT'S A NULL!") ? null : false;
            }
        };

        assertEquals("IT'S A NULL!", f1.apply(null));
        assertEquals("It's not null.", f1.apply(81));
        assertNull(f2.apply("IT'S A NULL!"));
        assertNotNull(f2.apply("It's a nulle."));

        assertNull(f1.compose(f2).apply(null));
        assertNotNull(f1.compose(f2).apply(18));
    }
}