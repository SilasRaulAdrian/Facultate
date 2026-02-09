package org.example.service;

import org.example.domain.ComplexNumber;
import org.example.domain.Operation;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class ExpressionParser {
    private static ExpressionParser instance = null;

    private ExpressionParser() {}

    public static ExpressionParser getInstance() {
        if (instance == null) {
            instance = new ExpressionParser();
        }

        return instance;
    }

    public List<Object> parse(final String s) {
        final List<Object> tokens = new ArrayList<>();
        final Stack<Operation> operations = new Stack<>();

        for (var part : s.trim().split(" ")) {
            part = part.trim();
            if (isOperator(part)) {
                var operation = Operation.fromOperator(part);
                while (!operations.isEmpty() && (operations.peek().getPrecedence() >= operation.getPrecedence())) {
                    tokens.add(operations.pop());
                }

                operations.push(operation);
                continue;
            }

            // Handle complex number
            var num = parseComplexNumber(part);
            tokens.add(num);
        }

        while (!operations.isEmpty()) {
            tokens.add(operations.pop());
        }

        return tokens;
    }

    // <num>+-<num>i
    public ComplexNumber parseComplexNumber(final String s) {
        final var trimmedS = s.trim();
        final var parts = trimmedS.split("[+-]");

        final var minusIndex = s.indexOf('-');
        final var isNegative = minusIndex != -1;

        if (parts.length == 1) {
            if (isImaginary(parts[0])) {
                final var c = new ComplexNumber(0, Double.parseDouble(parts[0].replace("i", "")));
                return isNegative ? c.conjugate() : c;
            }
            return new ComplexNumber(Double.parseDouble(parts[0]));
        }

        if (parts.length == 2) {
            final var real = Double.parseDouble(parts[0]);
            final var imaginary = Double.parseDouble(parts[1].replace("i", ""));
            final var c = new ComplexNumber(real, imaginary);
            return isNegative ? c.conjugate() : c;
        }

        throw new RuntimeException();
    }

    public boolean isOperator(final String s) {
        return "+-*/".contains(s);
    }

    public boolean isImaginary(final String s) {
        return s.endsWith("i");
    }
}
