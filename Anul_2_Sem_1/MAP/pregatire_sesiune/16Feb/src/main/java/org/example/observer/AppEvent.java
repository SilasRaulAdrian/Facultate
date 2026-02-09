package org.example.observer;

import org.example.domain.Nevoie;
import org.example.domain.Persoana;

public record AppEvent(AppEventType type, Persoana persoana, Nevoie nevoie) {
}
