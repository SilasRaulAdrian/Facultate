package org.example.observer;

import java.util.ArrayList;
import java.util.List;

/**
 * Interfata pentru Observable
 */
public interface Observable {
    List<Observer> observers = new ArrayList<>();

    /**
     * Metoda pentru adaugarea unui observator
     * @param observer Observer, observatorul care va fi adaugat
     */
    default void addObserver(Observer observer) {
        observers.add(observer);
    }

    /**
     * Metoda pentru eliminarea unui observator
     * @param observer Observer, observatorul care va fi eliminat
     */
    default void removeObserver(Observer observer) {
        observers.remove(observer);
    }

    /**
     * Metoda pentru notificarea observatorilor
     * @param signal Signal, semnalul care va fi trimis observatorilor
     */
    default void notifyObservers(Signal signal) {
        observers.forEach(o -> o.update(signal));
    }
}
