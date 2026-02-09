package org.example.exceptions;

/**
 * Clasa pentru exceptia de prietenie deja existenta
 */
public class FriendshipAlreadyExistsException extends RuntimeException {
    /**
     * Constructor pentru exceptia de prietenie deja existenta
     * @param userId1 id-ul primului utilizator
     * @param userId2 id-ul celui de-al doilea utilizator
     */
    public FriendshipAlreadyExistsException(Long userId1, Long userId2) {
        super("Friendship between users with ids " + userId1 + " and " + userId2 + " already exists.");
    }
}
