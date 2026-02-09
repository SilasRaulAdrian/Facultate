package org.example.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ComplexNumberTest {
    private final static double EPSILON = 1e-3;

    @Test
    void add() {
        // Arrange
        final ComplexNumber a = new ComplexNumber(3, 23);
        final ComplexNumber b = new ComplexNumber(1, 38.2349);
        final ComplexNumber result = new ComplexNumber(4, 61.2345);

        // Act
        final ComplexNumber c = a.add(b);

        // Assert
        assertEquals(result.getReal(), c.getReal(), EPSILON);
        assertEquals(result.getImaginary(), c.getImaginary(), EPSILON);
    }

    @Test
    void sub() {
        // Arrange
        final ComplexNumber a = new ComplexNumber(3, 23);
        final ComplexNumber b = new ComplexNumber(1, 38.2349);
        final ComplexNumber result = new ComplexNumber(2, -15.2349);

        // Act
        final ComplexNumber c = a.sub(b);

        // Assert
        assertEquals(result.getReal(), c.getReal(), EPSILON);
        assertEquals(result.getImaginary(), c.getImaginary(), EPSILON);
    }

    @Test
    void conjugate() {
        // Arrange
        final ComplexNumber num = new ComplexNumber(3, 23);
        final ComplexNumber result = new ComplexNumber(3, -23);

        // Act
        final ComplexNumber actual = num.conjugate();

        // Assert
        assertEquals(result.getReal(), actual.getReal());
        assertEquals(result.getImaginary(), actual.getImaginary());
    }

    @Test
    void mul() {
        // Arrange
        final ComplexNumber a = new ComplexNumber(3, 23);
        final ComplexNumber b = new ComplexNumber(1, 38.2349);
        final ComplexNumber result = new ComplexNumber(-876.4027, 137.7047);

        // Act
        final ComplexNumber c = a.mul(b);

        // Assert
        assertEquals(result.getReal(), c.getReal(), EPSILON);
        assertEquals(result.getImaginary(), c.getImaginary(), EPSILON);
    }

    @Test
    void div() {
        // Arrange
        final ComplexNumber a = new ComplexNumber(3, 23);
        final ComplexNumber b = new ComplexNumber(1, 38.2349);
        final ComplexNumber result = new ComplexNumber(0.60318, -0.06268);

        // Act
        final ComplexNumber c = a.div(b);

        // Assert
        assertEquals(result.getReal(), c.getReal(), EPSILON);
        assertEquals(result.getImaginary(), c.getImaginary(), EPSILON);
    }

    @Test
    void divByZero() {
        // Arrange
        final ComplexNumber a = new ComplexNumber(3, 23);
        final ComplexNumber b = ComplexNumber.ZERO();

        // Act
        final ComplexNumber c = a.div(b);
        assertThrowsExactly(DivByZeroNoGood.class, () -> a.div(b));

        // Assert
//        assertEquals(result.getReal(), c.getReal(), EPSILON);
//        assertEquals(result.getImaginary(), c.getImaginary(), EPSILON);
    }
}