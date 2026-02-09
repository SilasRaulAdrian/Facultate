package org.example.exceptions;

/**
 * Clasa pentru exceptia de utilizator duplicat
 */
public class DuplicateUserException extends RuntimeException {
    /**
     * Constructor pentru exceptia de utilizator duplicat
     * @param username username-ul utilizatorului duplicat
     */
    public DuplicateUserException(String username) {
        super("User with username '" + username + "' already exists.");
    }
}
