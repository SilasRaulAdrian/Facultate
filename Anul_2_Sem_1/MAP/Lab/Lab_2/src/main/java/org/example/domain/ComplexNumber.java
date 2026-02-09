package org.example.domain;

public class ComplexNumber {
    private final static double EPSILON = 1e-3;

    private final double real;
    private final double imaginary;

    public ComplexNumber(double real, double imaginary) {
        this.real = real;
        this.imaginary = imaginary;
    }

    public ComplexNumber(double real) {
        this.real = real;
        this.imaginary = 0f;
    }

    public ComplexNumber() {
        this.real = 0f;
        this.imaginary = 0f;
    }

    public static ComplexNumber ZERO() {
        return new ComplexNumber();
    }

    public double getReal() {
        return real;
    }

    public double getImaginary() {
        return imaginary;
    }

    public ComplexNumber add(ComplexNumber other) {
        return new ComplexNumber(
                this.real + other.getReal(),
                this.imaginary + other.getImaginary()
        );
    }

    public ComplexNumber sub(ComplexNumber other) {
        return new ComplexNumber(
                this.real - other.getReal(),
                this.imaginary - other.getImaginary()
        );
    }

    public ComplexNumber conjugate() {
        return new ComplexNumber(this.real, -this.imaginary);
    }

    public ComplexNumber mul(ComplexNumber other) {
        return new ComplexNumber(
            this.real * other.getReal() - this.imaginary * other.getImaginary(),
            this.real * other.getImaginary() + this.imaginary * other.getReal()
        );
    }

    public ComplexNumber div(ComplexNumber other) {
        final double denominator = other.getReal() * other.getReal() +
                other.getImaginary() * other.getImaginary();

        if (Double.compare(denominator, EPSILON) < 0) {
            throw new DivByZeroNoGood();
        }

        return new ComplexNumber(
            (this.real * other.getReal() + this.imaginary * other.getImaginary()) / denominator,
            (this.imaginary * other.getReal() - this.real * other.getImaginary()) / denominator
        );
    }

    @Override
    public String toString() {
        return real + imaginary + "*i";
    }
}
