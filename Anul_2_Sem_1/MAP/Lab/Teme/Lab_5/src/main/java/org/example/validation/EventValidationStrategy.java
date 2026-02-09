package org.example.validation;

import org.example.event.Event;
import org.example.model.User;

/**
 * Clasa pentru validarea evenimentelor
 */
public class EventValidationStrategy implements ValidationStrategy {
    /**
     * Metoda pentru validarea evenimentelor
     * @param args Object..., argumentele necesare pentru validare
     * @throws IllegalArgumentException daca validarea esueaza
     */
    @Override
    public void validate(Object... args) throws IllegalArgumentException {
        if (args.length != 1 || !(args[0] instanceof Event event)) {
            throw new IllegalArgumentException("Invalid arguments for card validation");
        }

        if (event.getSubscribers() == null)
            throw new IllegalArgumentException("Subscriber list cannot be null.");

        for (User user : event.getSubscribers()) {
            if (user == null)
                throw new IllegalArgumentException("Subscriber cannot be null.");
        }

        long distinctCount = event.getSubscribers().stream().distinct().count();
        if (distinctCount != event.getSubscribers().size())
            throw new IllegalArgumentException("Duplicate subscribers found in event.");
    }
}
