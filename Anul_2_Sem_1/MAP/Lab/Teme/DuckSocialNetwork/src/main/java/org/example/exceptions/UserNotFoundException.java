package org.example.exceptions;

/**
 * Clasa pentru exceptia de utilizator inexistent
 */
public class UserNotFoundException extends RuntimeException {
    /**
     * Constructor pentru exceptia de utilizator inexistent
     * @param id id-ul utilizatorului
     */
    public UserNotFoundException(Long id) {
        super("User with id " + id + " not found.");
    }

    /**
     * Constructor pentru exceptia de utilizator inexistent
     * @param username username-ul utilizatorului
     */
    public UserNotFoundException(String username) {
        super("User with username '" + username + "' not found");
    }
}
