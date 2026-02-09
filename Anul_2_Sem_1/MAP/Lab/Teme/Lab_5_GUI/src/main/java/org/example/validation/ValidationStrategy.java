package org.example.validation;

/**
 * Interfata pentru strategiile de validare
 */
public interface ValidationStrategy {
    /**
     * Metoda pentru validarea argumentelor
     * @param args argumentele de validat
     * @throws IllegalArgumentException daca validarea esueaza
     */
    void validate(Object... args) throws IllegalArgumentException;
}
