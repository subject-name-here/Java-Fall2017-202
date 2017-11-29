package ru.spbau.mit.java.paradov;

/**
 * This class helps to parse expression from string in infix notation
 * to stack with reverse Polish notation (RPN).
 */
public class ExpressionParser {
    /**
     * Turns infix notation expression to RPN stack.
     * @param expression expression to cast
     * @return expression in RPN in stack
     * @throws IncorrectExpressionException when expression has wrong symbols, or
     * when it has bad bracing
     */
    public static MyStack<String> expressionToRPNStack(String expression)
            throws IncorrectExpressionException {
        MyStack<String> expressionParts = new MyStack<>();
        String operators = "()+-*/";
        boolean wasDigit = false;
        int current = 0;

        for (int i = 0; i < expression.length(); i++) {
            Character c = expression.charAt(i);
            if (Character.isDigit(c)) {
                if (wasDigit) {
                    current = current * 10 + Character.getNumericValue(c);
                } else {
                    wasDigit = true;
                    current = Character.getNumericValue(c);
                }

            } else if (Character.isSpaceChar(c) || operators.indexOf(c) != -1) {
                if (wasDigit) {
                    expressionParts.push(Integer.toString(current));
                    wasDigit = false;
                }

                if (operators.indexOf(c) != -1)
                    expressionParts.push("" + c);

            } else {
                throw new IncorrectExpressionException();
            }
        }
        if (wasDigit)
            expressionParts.push(Integer.toString(current));

        expressionParts.reverse();
        MyStack<String> reversePolishNotationExpression = new MyStack<>();
        MyStack<String> operations = new MyStack<>();

        while (!expressionParts.isEmpty()) {
            String s = expressionParts.pop();
            if (s.matches("^-?\\d+$")) {
                reversePolishNotationExpression.push(s);
            } else if (s.equals(")")) {
                String tmp = operations.pop();
                while (tmp != null && !tmp.equals("(")) {
                    reversePolishNotationExpression.push(tmp);
                    tmp = operations.pop();
                }
                if (tmp == null)
                    throw new IncorrectExpressionException();
            } else if (s.equals("(")) {
                operations.push(s);
            } else {
                String tmp = operations.pop();
                int priorityOfOperator = operators.indexOf(s) / 2;

                while (tmp != null) {
                    int priorityOfTmp = operators.indexOf(tmp) / 2;
                    if (priorityOfOperator > priorityOfTmp)
                        break;

                    reversePolishNotationExpression.push(tmp);
                    tmp = operations.pop();
                }
                if (tmp != null)
                    operations.push(tmp);
                operations.push(s);
            }

        }

        while (!operations.isEmpty()) {
            String tmp = operations.pop();
            if (tmp.equals("(") || tmp.equals(")")) {
                throw new IncorrectExpressionException();
            }
            reversePolishNotationExpression.push(tmp);
        }


        return reversePolishNotationExpression;
    }

}
