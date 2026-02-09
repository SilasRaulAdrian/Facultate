package org.example.exceptions;

/**
 * Clasa pentru exceptia EventNotFoundException
 */
public class EventNotFoundException extends RuntimeException {
    /**
     * Constructor pentru EventNotFoundException
     * @param id Long, id-ul eventului care nu a fost gasit
     */
    public EventNotFoundException(Long id) {
        super("Event with ID " + id + " not found.");
    }
}
