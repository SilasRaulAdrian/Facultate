package org.example.observer;

import org.example.domain.River;

public record AnarEvent(AnarEventType type, River river) {
}
