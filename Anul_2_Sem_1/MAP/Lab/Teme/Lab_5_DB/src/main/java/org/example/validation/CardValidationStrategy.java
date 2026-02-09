package org.example.validation;

import org.example.model.Card;
import org.example.model.Duck;

/**
 * Clasa pentru validarea obiectelor de tip Card
 */
public class CardValidationStrategy implements ValidationStrategy {
    /**
     * Metoda pentru validarea unui obiect de tip Card
     * @param args Object..., argumentele necesare pentru validare
     * @throws IllegalArgumentException daca validarea esueaza
     */
    @Override
    public void validate(Object... args) throws IllegalArgumentException {
        if (args.length != 1 || !(args[0] instanceof Card<?> card)) {
            throw new IllegalArgumentException("Invalid arguments for card validation");
        }

        if (card.getId() <= 0) {
            throw new IllegalArgumentException("Card ID cannot be null and must be positive");
        }

        if (card.getNumeCard() == null || card.getNumeCard().isBlank())
            throw new IllegalArgumentException("Card name cannot be empty.");

        if (card.getMembri() == null)
            throw new IllegalArgumentException("Member list cannot be null.");

        for (Object member : card.getMembri()) {
            if (!(member instanceof Duck))
                throw new IllegalArgumentException("All members must be of type Duck.");
        }

        long distinctCount = card.getMembri().stream().distinct().count();
        if (distinctCount != card.getMembri().size())
            throw new IllegalArgumentException("Duplicate ducks found in card members.");
    }
}
