package org.example.exceptions;

/**
 * Clasa pentru exceptia de prietenie inexistenta
 */
public class FriendshipNotFoundException extends RuntimeException {
    /**
     * Constructor pentru exceptia de prietenie inexistenta
     * @param id1 id-ul primului utilizator
     * @param id2 id-ul celui de-al doilea utilizator
     */
    public FriendshipNotFoundException(Long id1, Long id2) {
        super("Friendship between users with ids " + id1 + " and " + id2 + " not found.");
    }
}
