package org.example.domain;

public enum Operation {
    ADD,
    SUB,
    MUL,
    DIV;

    public static Operation fromOperator(final String op) {
        return switch (op) {
            case "+" -> ADD;
            case "-" -> SUB;
            case "/" -> DIV;
            case "*" -> MUL;
            default -> throw new IllegalArgumentException("unknown symbol: " + op);
        };
    }

    public int getPrecedence() {
        return switch (this) {
            case ADD, SUB -> 1;
            case MUL, DIV -> 2;
            default -> throw new IllegalArgumentException("unknown op");
        };
    }
}
