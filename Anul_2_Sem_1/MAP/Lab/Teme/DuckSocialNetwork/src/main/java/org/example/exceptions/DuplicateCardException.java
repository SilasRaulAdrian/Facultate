package org.example.exceptions;

/**
 * Exceptie aruncata atunci cand se incearca adaugarea unui card care exista deja
 */
public class DuplicateCardException extends RuntimeException {
    /**
     * Constructor pentru DuplicateCardException
     * @param name Numele cardului duplicat
     */
    public DuplicateCardException(String name) {
        super("Cardul cu numele '" + name + "' exista deja!");
    }
}
