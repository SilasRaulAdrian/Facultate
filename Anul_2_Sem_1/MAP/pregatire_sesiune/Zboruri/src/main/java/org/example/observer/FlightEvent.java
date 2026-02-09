package org.example.observer;

public record FlightEvent(FlightEventType type, long flightId) {
}
