package org.example.validation;

import org.example.event.RaceEvent;
import org.example.model.Lane;
import org.example.model.SwimmingDuck;

/**
 * Clasa pentru validarea evenimentelor de tip RaceEvent
 */
public class RaceEventValidationStrategy implements ValidationStrategy {
    /**
     * Metoda pentru validarea argumentelor unui eveniment de tip RaceEvent
     * @param args argumentele de validat
     * @throws IllegalArgumentException daca argumentele nu sunt valide
     */
    @Override
    public void validate(Object... args) throws IllegalArgumentException {
        if (args.length != 1 || !(args[0] instanceof RaceEvent raceEvent)) {
            throw new IllegalArgumentException("Invalid arguments for card validation");
        }

        if (raceEvent.getDucks() == null || raceEvent.getDucks().isEmpty())
            throw new IllegalArgumentException("RaceEvent must have at least one duck.");

        if (raceEvent.getLanes() == null || raceEvent.getLanes().isEmpty())
            throw new IllegalArgumentException("RaceEvent must have at least one lane.");

        for (SwimmingDuck duck : raceEvent.getDucks()) {
            if (duck == null)
                throw new IllegalArgumentException("Duck cannot be null.");
            if (duck.getViteza() <= 0)
                throw new IllegalArgumentException("Duck speed must be positive.");
            if (duck.getRezistenta() <= 0)
                throw new IllegalArgumentException("Duck endurance must be positive.");
        }

        for (Lane lane : raceEvent.getLanes()) {
            if (lane == null)
                throw new IllegalArgumentException("Lane cannot be null.");
            if (lane.getDistance() <= 0)
                throw new IllegalArgumentException("Lane distance must be positive.");
        }
    }
}
