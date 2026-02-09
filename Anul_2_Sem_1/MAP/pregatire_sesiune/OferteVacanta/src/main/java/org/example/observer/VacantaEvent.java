package org.example.observer;

public record VacantaEvent(VacantaEventType type, Long targetClientId, String message) {
}
