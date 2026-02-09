package org.example.service;

import org.example.domain.ComplexNumber;
import org.example.domain.Operation;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ExpressionParserTest {

    @Test
    void parseComplexNumber() {
        // Arrange
        final var instance = ExpressionParser.getInstance();
        final var result = new ComplexNumber(3, -4);

        // Act
        final var actual = instance.parseComplexNumber("3-4i");

        // Assert
        assertEquals(result.getReal(), actual.getReal());
        assertEquals(result.getImaginary(), actual.getImaginary());
    }

    @Test
    void parseComplexNumber2() {
        // Arrange
        final var instance = ExpressionParser.getInstance();
        final var result = new ComplexNumber(3);

        // Act
        final var actual = instance.parseComplexNumber("3");

        // Assert
        assertEquals(result.getReal(), actual.getReal());
        assertEquals(result.getImaginary(), actual.getImaginary());
    }

    @Test
    void isImaginary() {
        // Arrange
        final var instance = ExpressionParser.getInstance();

        // Act
        final var actual = instance.isImaginary("3+4i");
        final var actual2 = instance.isImaginary("3+4");

        // Assert
        assertEquals(true, actual);
        assertEquals(false, actual2);
    }

    @Test
    void parse() {
        // Arrange
        final var input = "3 + 4 * 5 / 2";
        final var expected = List.of(
                new ComplexNumber(3),
                new ComplexNumber(4),
                new ComplexNumber(5),
                Operation.MUL,
                new ComplexNumber(2),
                Operation.DIV,
                Operation.ADD
        );

        // Act
        final var actual = ExpressionParser.getInstance().parse(input);

        assertEquals(expected.size(), actual.size());
        for (int i = 0; i < actual.size(); i++) {
            var expectedEl = expected.get(i);
            var actualEl = actual.get(i);

            if (expectedEl instanceof ComplexNumber && actualEl instanceof ComplexNumber) {
                var expectedNum = (ComplexNumber) expectedEl;
                var actualNum = (ComplexNumber) actualEl;
                assertEquals(expectedNum.getReal(), actualNum.getReal());
                assertEquals(expectedNum.getImaginary(), actualNum.getImaginary());
            } else if (expectedEl instanceof Operation && actualEl instanceof Operation) {
                assertEquals(expectedEl, actualEl);
            } else {
                throw new IllegalStateException("mismatched types");
            }
        }
    }
}