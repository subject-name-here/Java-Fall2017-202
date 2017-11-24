package ru.spbau.mit.java.paradov;

import org.junit.Test;

import static org.junit.Assert.*;

/** Class that tests functionality of Function2 class. */
public class Function2Test {
    /** Tests if apply() of a function works as expected. */
    @Test
    public void testApplyWorksCorrectly() {
        Function2<Integer, Integer, Character> f = (arg1, arg2) -> arg1 + arg2 == 42 ? 'T' : 'F';

        assertEquals('T', (long) f.apply(20, 22));
        assertEquals('T', (long) f.apply(0, 42));
        assertEquals('F', (long) f.apply(0, 0));
    }

    /** Tests if composition of functions works as expected. */
    @Test
    public void testApplyOfCompositionWorksCorrectly() {
        Function1<String, Character> f1 = arg -> {
            switch (arg) {
                case "Even":
                    return 'T';
                case "Odd":
                    return 'F';
                default:
                    return 'N';
            }
        };

        Function2<Boolean, Integer, String> f2 = (arg1, arg2) -> {
            if (!arg1) {
                return "Cannot analyze";
            } else if (arg2 % 2 == 0) {
                return "Even";
            } else {
                return "Odd";
            }
        };


        Function2<Boolean, Integer, Character> f = f2.compose(f1);
        assertEquals('T', (long) f.apply(true, 42));
        assertEquals('F', (long) f.apply(true, 43));
        assertEquals('N', (long) f.apply(false, null));
    }

    /** Tests if bind1() works as expected. */
    @Test
    public void testBind1WorksCorrectly(){
        Function2<Boolean, Integer, String> checker = (arg1, arg2) -> {
            if (arg1) {
                if (arg2 % 2 == 0) {
                    return "Even";
                } else {
                    return "Odd";
                }
            } else {
                if (arg2 >= 0) {
                    return "Non-negative";
                } else {
                    return "Negative";
                }
            }
        };

        Function1<Integer, String> evenChecker = checker.bind1(true);
        Function1<Integer, String> negativeChecker = checker.bind1(false);

        assertEquals("Even", evenChecker.apply(-22));
        assertEquals("Odd", evenChecker.apply(-21));

        assertEquals("Negative", negativeChecker.apply(-22));
        assertEquals("Non-negative", negativeChecker.apply(22));
    }

    /** Tests if bind2() works as expected. */
    @Test
    public void testBind2WorksCorrectly(){
        Function2<Integer, Integer, String> power = (arg1, arg2) -> {
            Integer result = 1;
            for (int i = 0; i < arg2; i++) {
                result *= arg1;
            }
            return result.toString();
        };

        Integer k = 2;
        Function1<Integer, String> squareNum = power.bind2(k);

        assertEquals("9", squareNum.apply(3));
    }

    /** Tests if curry() works as expected. */
    @Test
    public void testCurryWorksCorrectly(){
        Function2<Integer, Integer, String> power = (arg1, arg2) -> {
            Integer result = 1;
            for (int i = 0; i < arg2; i++) {
                result *= arg1;
            }
            return result.toString();
        };

        Function1<Integer, String> powerOfTwo = power.curry(2);
        assertEquals("1", powerOfTwo.apply(0));
        assertEquals("8", powerOfTwo.apply(3));
        assertEquals("64", powerOfTwo.apply(6));
        assertEquals("4096", powerOfTwo.apply(12));
    }
}