package ru.spbau.mit.java.paradov;

/**
 * This class can calculate an expressions in reverse polish notation, which is packed
 * into MyStack<String> (first element of expression will be popped from stack first).
 * After one calculation, method calculate() returns result that once was counted.
 */
public class Calculator {
    /** Stack where expression is stored. */
    private MyStack<String> reversePolishNotationStack;
    /** Result of a calculation. Need it to not solve this thing second time. */
    private int result = 0;

    public Calculator(MyStack<String> stack) {
        reversePolishNotationStack = stack;
    }

    /**
     * Primary function that does all work - calculate expression that was given to Calculator.
     * After it, stack that we keep, is empty.
     * @return result of an expression
     * @throws IncorrectExpressionException if expression was incorrect
     */
    public int calculate() throws IncorrectExpressionException {
        if (reversePolishNotationStack.isEmpty()) {
            return result;
        }
        reversePolishNotationStack.reverse();

        MyStack<Integer> numberStack = new MyStack<>();

        while (!reversePolishNotationStack.isEmpty()) {
            String s = reversePolishNotationStack.pop();

            if (s.matches("^-?\\d+$")) {
                numberStack.push(Integer.parseInt(s));
            } else {
                Integer n2 = numberStack.pop();
                Integer n1 = numberStack.pop();

                if (n1 == null || n2 == null) {
                    throw new IncorrectExpressionException();
                }

                switch (s) {
                    case "+":
                        numberStack.push(n1 + n2);
                        break;
                    case "-":
                        numberStack.push(n1 - n2);
                        break;
                    case "*":
                        numberStack.push(n1 * n2);
                        break;
                    case "/":
                        numberStack.push(n1 / n2);
                        break;
                }
            }
        }

        if (numberStack.size() != 1) {
            throw new IncorrectExpressionException();
        } else {
            result = numberStack.pop();
            return result;
        }
    }

}
