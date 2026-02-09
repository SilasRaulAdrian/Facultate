package org.example.observer;

public interface Observer<E> {
    void update(E e);
}