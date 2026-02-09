package org.example.observer;

import org.example.model.Event;

public interface Observer<E extends Event> {
    void update(E e);
}
