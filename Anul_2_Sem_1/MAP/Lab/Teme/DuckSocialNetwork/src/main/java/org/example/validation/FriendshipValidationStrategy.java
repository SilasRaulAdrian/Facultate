package org.example.validation;

/**
 * Clasa pentru validarea prieteniei dintre doi utilizatori
 */
public class FriendshipValidationStrategy implements ValidationStrategy {
    /**
     * Valideaza daca doi utilizatori pot fi prieteni
     * @param args argumentele care trebuie validate
     * @throws IllegalArgumentException daca validarea esueaza
     */
    @Override
    public void validate(Object... args) throws IllegalArgumentException {
        if (args.length != 2) {
            throw new IllegalArgumentException("Exactly two arguments are required to validate a friendship.");
        }

        Object id1 = args[0];
        Object id2 = args[1];

        if (!(id1 instanceof Long) || !(id2 instanceof Long)) {
            throw new IllegalArgumentException("Both arguments must be of type Long representing user IDs.");
        }

        long userId1 = (Long) id1;
        long userId2 = (Long) id2;

        if (userId1 <= 0 || userId2 <= 0) {
            throw new IllegalArgumentException("User IDs must be positive numbers.");
        }

        if (userId1 == userId2) {
            throw new IllegalArgumentException("A user cannot be friends with themselves.");
        }
    }
}
