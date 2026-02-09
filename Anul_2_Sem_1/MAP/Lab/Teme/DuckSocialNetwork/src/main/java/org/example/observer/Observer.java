package org.example.observer;

/**
 * Interfata pentru observatori in pattern-ul Observer
 */
public interface Observer {
    /**
     * Metoda apelata pentru a notifica observatorul despre un nou semnal
     * @param signal Signal, semnalul care a fost emis
     */
    void update(Signal signal);
}
