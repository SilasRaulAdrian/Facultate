package org.example.observer;

import org.example.model.Event;

public interface Observable<E extends Event> {
    void add(Observer<E> e);

    void remove(Observer<E> e);

    void notify(E e);
}
