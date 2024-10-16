package org.example;

import java.util.*;
import java.util.function.Function;

public class HomeWork {
    Map<String, Function<Double[], Double>> functions;
    /**
     * Метод для вычисления математического выражения по строке.
     *
     * @param expr выражение, которое нужно вычислить
     * @return результат вычисления
     */
    public double calculate(String expr) {
        if (expr == null || expr.isEmpty()) {
            throw new IllegalArgumentException("Expression cannot be null or empty");
        }

        Stack<Double> values = new Stack<>();
        Stack<String> ops = new Stack<>();
        functions = initFunctions();

        String[] tokens = expr.split(" ");
        for (String token : tokens) {
            if (token.isEmpty()) {
                continue;
            }

            if (isNumber(token)) {
                values.push(Double.parseDouble(token));
            } else if (isFunction(token)) {
                ops.push(token);
            } else if (token.equals("(")) {
                ops.push(token);
            } else if (token.equals(")")) {
                while (!ops.peek().equals("(")) {
                    applyOperation(ops, values, functions);
                }
                ops.pop(); // Убираем '('
                if (!ops.isEmpty() && isFunction(ops.peek())) {
                    applyOperation(ops, values, functions);
                }
            } else if (isOperator(token)) {
                while (!ops.isEmpty() && precedence(ops.peek()) >= precedence(token)) {
                    applyOperation(ops, values, functions);
                }
                ops.push(token);
            }
        }

        while (!ops.isEmpty()) {
            applyOperation(ops, values, functions);
        }

        return values.pop();
    }

    private boolean isNumber(String token) {
        try {
            Double.parseDouble(token);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private boolean isOperator(String token) {
        return token.equals("+") || token.equals("-") || token.equals("*") || token.equals("/");
    }

    private boolean isFunction(String token) {
        return token.equals("sin") || token.equals("cos") || token.equals("sqr") || token.equals("pow");
    }

    private int precedence(String op) {
        switch (op) {
            case "+":
            case "-":
                return 1;
            case "*":
            case "/":
                return 2;
            case "sin":
            case "cos":
            case "sqr":
            case "pow":
                return 3;
            default:
                return 0;
        }
    }

    private void applyOperation(Stack<String> ops, Stack<Double> values, Map<String, Function<Double[], Double>> functions) {
        String op = ops.pop();
        if (isOperator(op)) {
            double b = values.pop();
            double a = values.pop();
            values.push(performOperation(a, b, op));
        } else if (functions.containsKey(op)) {
            Function<Double[], Double> function = functions.get(op);
            Double[] args = popArguments(values, function);
            values.push(function.apply(args));
        }
    }

    private double performOperation(double a, double b, String op) {
        switch (op) {
            case "+":
                return a + b;
            case "-":
                return a - b;
            case "*":
                return a * b;
            case "/":
                if (b == 0) {
                    throw new ArithmeticException("Division by zero");
                }
                return a / b;
            default:
                throw new IllegalArgumentException("Unknown operator: " + op);
        }
    }

    private Double[] popArguments(Stack<Double> values, Function<Double[], Double> function) {
        if (function == functions.get("pow")) {
            return new Double[]{values.pop(), values.pop()};
        }
        return new Double[]{values.pop()};
    }

    private Map<String, Function<Double[], Double>> initFunctions() {
        Map<String, Function<Double[], Double>> functions = new HashMap<>();
        functions.put("sin", args -> Math.sin(args[0]));
        functions.put("cos", args -> Math.cos(args[0]));
        functions.put("sqr", args -> Math.pow(args[0], 2));
        functions.put("pow", args -> Math.pow(args[1], args[0]));
        return functions;
    }
}
