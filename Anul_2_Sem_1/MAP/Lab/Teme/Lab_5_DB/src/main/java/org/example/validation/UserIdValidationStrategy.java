package org.example.validation;

/**
 * Clasa pentru strategia de validare a ID-ului utilizatorului
 */
public class UserIdValidationStrategy implements ValidationStrategy {
    /**
     * Valideaza daca ID-ul utilizatorului este valid
     * @param args argumentele necesare pentru validare
     * @throws IllegalArgumentException daca validarea esueaza
     */
    @Override
    public void validate(Object... args) throws IllegalArgumentException {
        if (args.length == 0 || !(args[0] instanceof Long userId)) {
            throw new IllegalArgumentException("Invalid argument. Expected a Long user ID.");
        }

        if (userId <= 0) {
            throw new IllegalArgumentException("User ID must be a positive number.");
        }
    }
}
