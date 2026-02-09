package org.example.validation;

import org.example.model.Duck;

/**
 * Clasa pentru validarea obiectelor de tip Duck
 */
public class DuckValidationStrategy implements ValidationStrategy {
    /**
     * Valideaza un obiect de tip Duck
     * @param args parametrii necesari pentru validare
     * @throws IllegalArgumentException daca validarea esueaza
     */
    @Override
    public void validate(Object... args) throws IllegalArgumentException {
        if (args.length == 0 || !(args[0] instanceof Duck duck)) {
            throw new IllegalArgumentException("Invalid user type. Expected Duck.");
        }

        if (duck.getUsername() == null || duck.getUsername().isBlank()) {
            throw new IllegalArgumentException("Username cannot be empty.");
        }

        if (duck.getEmail() == null || duck.getEmail().isBlank() ||
                !duck.getEmail().contains("@") || !duck.getEmail().contains(".")) {
            throw new IllegalArgumentException("Email address cannot be empty or it is invalid.");
        }

        if (duck.getPassword() == null || duck.getPassword().isBlank()) {
            throw new IllegalArgumentException("Password cannot be empty.");
        }

        if (duck.getViteza() <= 0) {
            throw new IllegalArgumentException("Speed must be a positive number.");
        }

        if (duck.getRezistenta() <= 0) {
            throw new IllegalArgumentException("Resilience must be a positive number.");
        }

        if (duck.getTip() == null || duck.getTip().toString().isBlank() ||
                !(duck.getTip().toString().equals("FLYING") ||
                  duck.getTip().toString().equals("SWIMMING") ||
                  duck.getTip().toString().equals("FLYING_AND_SWIMMING"))) {
            throw new IllegalArgumentException("Type of duck cannot be empty and must be one of the predefined types.");
        }
    }
}
