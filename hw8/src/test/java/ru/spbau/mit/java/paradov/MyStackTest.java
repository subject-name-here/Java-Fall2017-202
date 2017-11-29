package ru.spbau.mit.java.paradov;

import org.junit.Test;

import static org.junit.Assert.*;

/** Tests functionality of class MyStack. */
public class MyStackTest {
    /** Tests if empty stack is really empty. */
    @Test
    public void testSizeOfEmptyStack() {
        assertTrue(new MyStack<Integer>().isEmpty());
    }

    /** Tests if push() changes size correctly. */
    @Test
    public void testPushChangesSize() {
        MyStack<Integer> stack = new MyStack<>();
        stack.push(2);
        stack.push(4);
        stack.push(6);
        stack.push(8);
        assertEquals(4, stack.size());
    }

    /** Tests if pop() changes size correctly. */
    @Test
    public void testPopChangesSize() {
        MyStack<Integer> stack = new MyStack<>();
        stack.push(2);
        stack.push(4);
        stack.push(6);
        stack.push(8);
        stack.push(10);

        stack.pop();
        stack.pop();

        assertEquals(3, stack.size());
    }

    /** Tests if pop() returns right elements. */
    @Test
    public void testPopReturnsRightElements() {
        MyStack<Integer> stack = new MyStack<>();
        stack.push(2);
        stack.push(4);
        stack.push(6);
        stack.push(8);

        assertEquals(8, (int) stack.pop());
        assertEquals(6, (int) stack.pop());
    }

    /** Tests if pop() doesn't throw exceptions when calles on empty stack. */
    @Test
    public void testPopDoesNotThrowException() {
        MyStack<Integer> stack = new MyStack<>();
        assertNull(stack.pop());

        stack.push(2);
        assertEquals(2, (int) stack.pop());
        assertNull(stack.pop());
    }

    /** Tests if reverse() works as expected. */
    @Test
    public void testReverseWorksCorrectly() {
        MyStack<Integer> stack = new MyStack<>();
        stack.push(2);
        stack.push(4);
        stack.push(6);
        stack.push(8);

        stack.reverse();
        assertEquals(2, (int) stack.pop());
        assertEquals(4, (int) stack.pop());
    }

}