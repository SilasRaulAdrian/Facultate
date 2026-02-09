package org.example.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@RequiredArgsConstructor
public class EntityChangedEvent<E> implements Event {
    public enum EventType {
        ADD,
        UPDATE,
        DELETE
    }

    private final EventType type;
    private final E data;
}
