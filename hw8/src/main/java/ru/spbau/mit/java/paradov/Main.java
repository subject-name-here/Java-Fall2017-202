package ru.spbau.mit.java.paradov;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        System.out.println("Enter the expression, using only natural numbers, 0, +, -, *, /, (, ).");

        Scanner sc = new Scanner(System.in);
        String expression = sc.nextLine();

        MyStack<String> reversePolishNotationStack;

        try {
            reversePolishNotationStack = ExpressionParser.expressionToRPNStack(expression);

            Calculator calculator = new Calculator(reversePolishNotationStack);
            System.out.println(calculator.calculate());

        } catch (IncorrectExpressionException e) {
            System.out.println("You gave incorrect expression.");
        }

    }
}
