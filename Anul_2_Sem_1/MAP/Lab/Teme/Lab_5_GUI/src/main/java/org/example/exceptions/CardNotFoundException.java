package org.example.exceptions;

/**
 * Exceptie aruncata atunci cand un card nu este gasit
 */
public class CardNotFoundException extends RuntimeException {
    /**
     * Constructor pentru CardNotFoundException
     * @param id Long, id-ul cardului care nu a fost gasit
     */
    public CardNotFoundException(Long id) {
        super("Card with ID " + id + " not found.");
    }
}
