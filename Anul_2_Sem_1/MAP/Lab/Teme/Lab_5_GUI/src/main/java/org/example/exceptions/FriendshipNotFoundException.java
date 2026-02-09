package org.example.exceptions;

/**
 * Clasa pentru exceptia de prietenie inexistenta
 */
public class FriendshipNotFoundException extends RuntimeException {
    /**
     * Constructor pentru exceptia de prietenie inexistenta
     * @param id Long, id-ul prieteniei care nu a fost gasita
     */
    public FriendshipNotFoundException(Long id) {
        super("Friendship with id " + id + " not found!");
    }
}
