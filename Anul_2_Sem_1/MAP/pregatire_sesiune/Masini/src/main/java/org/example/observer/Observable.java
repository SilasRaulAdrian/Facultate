package org.example.observer;

public interface Observable<E> {
    void addObserver(Observer<E> o);
    void removeObserver(Observer<E> o);
    void notifyObservers(E event);
}
