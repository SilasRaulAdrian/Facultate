package org.example.event;

import org.example.model.Entity;
import org.example.model.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Clasa abstracta Event care reprezinta un eveniment in sistem
 */
public abstract class Event extends Entity<Long> {
    protected final List<User> subscribers = new ArrayList<>();

    /**
     * Metoda pentru abonarea unui utilizator la eveniment
     * @param user User, utilizatorul care se aboneaza
     */
    public void subscribe(User user) {
        if (!subscribers.contains(user)) {
            subscribers.add(user);
        }
    }

    /**
     * Metoda pentru dezabonarea unui utilizator de la eveniment
     * @param user User, utilizatorul care se dezaboneaza
     */
    public void unsubscribe(User user) {
        subscribers.remove(user);
    }

    /**
     * Metoda pentru obtinerea listei de abonati
     * @return List<User>, lista de abonati
     */
    public List<User> getSubscribers() {
        return subscribers;
    }

    /**
     * Metoda pentru notificarea tuturor abonatilor despre un mesaj
     * @param message String, mesajul de notificare
     */
    public void notifySubscribers(String message) {
        for (User user : subscribers) {
            if (user == null) {
                System.out.println("Null user found in subscribers list.");
                continue;
            }
            user.receiveMessage(message);
        }
    }
}
