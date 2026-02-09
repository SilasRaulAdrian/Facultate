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

    /**
     * Constructor pentru exceptia de prietenie inexistenta intre doi utilizatori
     * @param id1 Long, id-ul primului utilizator
     * @param id2 Long, id-ul celui de-al doilea utilizator
     */
    public FriendshipNotFoundException(Long id1, Long id2) {
        super("Friendship between users " + id1 + " and " + id2 + " not found!");
    }
}
