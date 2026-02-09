package org.example.observer;

import org.example.domain.Order;

public record RestaurantEvent(RestaurantEventType type, Order order) {
}
