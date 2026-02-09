package org.example.observer;

import org.example.domain.Order;

public record TaxiEvent(TaxiEventType type, Integer targetDriverId, Order order) {
}
