package org.example.observer;

public record ClinicEvent(ClinicEventType type, Integer targetMedicId) {
}
