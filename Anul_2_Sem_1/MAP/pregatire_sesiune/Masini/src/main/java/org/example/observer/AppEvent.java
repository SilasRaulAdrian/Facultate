package org.example.observer;

public record AppEvent(AppEventType type, Integer targetUserId, String message) {
}
