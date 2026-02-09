package org.example.domain;

import java.time.LocalDateTime;

public record PlacedOrderRow(int tableId, LocalDateTime date, String products) {
}
