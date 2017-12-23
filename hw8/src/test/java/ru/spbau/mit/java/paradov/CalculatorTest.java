package ru.spbau.mit.java.paradov;

import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/** Tests the only method of Calculator - calculate() - using mocks. */
public class CalculatorTest {
    /** Tests if calculation of empty expression returns 0.*/
    @Test
    public void testCalculateEmptyExpression() throws Exception {
        MyStack<String> stack = mock(MyStack.class);
        when(stack.isEmpty()).thenReturn(true);
        when(stack.pop()).thenReturn(null);

        Calculator calculator = new Calculator(stack);
        assertEquals(0, calculator.calculate());
    }

    /** Tests if small expression calculated right. */
    @Test
    public void testCalculateSmallExpression() throws Exception{
        MyStack<String> stack = mock(MyStack.class);
        when(stack.pop())
                .thenReturn("2")
                .thenReturn("3")
                .thenReturn("+");

        when(stack.isEmpty())
                .thenReturn(false)
                .thenReturn(false)
                .thenReturn(false)
                .thenReturn(false)
                .thenReturn(true);

        Calculator calculator = new Calculator(stack);
        assertEquals(5, calculator.calculate());
    }

    /** Tests if medium expression calculated right. */
    @Test
    public void testCalculateBiggerExpression() throws Exception{
        MyStack<String> stack = mock(MyStack.class);
        when(stack.pop())
                .thenReturn("2")
                .thenReturn("3")
                .thenReturn("+")
                .thenReturn("5")
                .thenReturn("*")
                .thenReturn("3")
                .thenReturn("+");

        when(stack.isEmpty())
                .thenReturn(false)
                .thenReturn(false)
                .thenReturn(false)
                .thenReturn(false)
                .thenReturn(false)
                .thenReturn(false)
                .thenReturn(false)
                .thenReturn(false)
                .thenReturn(true);

        Calculator calculator = new Calculator(stack);
        assertEquals(28, calculator.calculate());
    }

    /** Test if big, complicated expression calculated right and without exceptions. */
    @Test
    public void testCalculateBigExpression() throws Exception{
        MyStack<String> stack = mock(MyStack.class);
        when(stack.pop())
                .thenReturn("2")
                .thenReturn("3")
                .thenReturn("4")
                .thenReturn("5")
                .thenReturn("6")
                .thenReturn("7")
                .thenReturn("8")
                .thenReturn("9")
                .thenReturn("-")
                .thenReturn("+")
                .thenReturn("-")
                .thenReturn("*")
                .thenReturn("-")
                .thenReturn("+")
                .thenReturn("*");
        // 2*(3+(4-5*(6-(7+(8-9)))))

        when(stack.isEmpty())
                .thenReturn(false)
                .thenReturn(false)
                .thenReturn(false)
                .thenReturn(false)
                .thenReturn(false)
                .thenReturn(false)
                .thenReturn(false)
                .thenReturn(false)
                .thenReturn(false)
                .thenReturn(false)
                .thenReturn(false)
                .thenReturn(false)
                .thenReturn(false)
                .thenReturn(false)
                .thenReturn(false)
                .thenReturn(false)
                .thenReturn(true);

        Calculator calculator = new Calculator(stack);
        assertEquals(14, calculator.calculate());
    }

    /** Tests if expression with too many operators throws exception. */
    @Test(expected = IncorrectExpressionException.class)
    public void testThrowExceptionBecauseTooManyOps() throws Exception{
        MyStack<String> stack = mock(MyStack.class);
        when(stack.pop())
                .thenReturn("-")
                .thenReturn("+")
                .thenReturn("*");

        when(stack.isEmpty())
                .thenReturn(false)
                .thenReturn(false)
                .thenReturn(false)
                .thenReturn(false)
                .thenReturn(true);

        Calculator calculator = new Calculator(stack);
        calculator.calculate();
    }

    /** Tests if calculation throws exception if too many number on operations. */
    @Test(expected = IncorrectExpressionException.class)
    public void testThrowExceptionBecauseTooManyNums() throws Exception{
        MyStack<String> stack = mock(MyStack.class);
        when(stack.pop())
                .thenReturn("2")
                .thenReturn("6");

        when(stack.isEmpty())
                .thenReturn(false)
                .thenReturn(false)
                .thenReturn(false)
                .thenReturn(true);

        Calculator calculator = new Calculator(stack);
        calculator.calculate();
    }

    /** Tests if wrong order (not RPN) expression calculation throws exception. */
    @Test(expected = IncorrectExpressionException.class)
    public void testThrowExceptionBecauseWrongOrder() throws Exception{
        MyStack<String> stack = mock(MyStack.class);
        when(stack.pop())
                .thenReturn("2")
                .thenReturn("4")
                .thenReturn("*")
                .thenReturn("+")
                .thenReturn("6");

        when(stack.isEmpty())
                .thenReturn(false)
                .thenReturn(false)
                .thenReturn(false)
                .thenReturn(false)
                .thenReturn(false)
                .thenReturn(false)
                .thenReturn(true);

        Calculator calculator = new Calculator(stack);
        calculator.calculate();
    }

}