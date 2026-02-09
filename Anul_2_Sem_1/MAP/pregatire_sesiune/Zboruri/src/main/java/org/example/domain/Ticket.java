package org.example.domain;

import java.time.LocalDateTime;

public class Ticket {
    private final String username;
    private final long flightId;
    private final LocalDateTime purchaseTime;

    public Ticket(String username, long flightId, LocalDateTime purchaseTime) {
        this.username = username;
        this.flightId = flightId;
        this.purchaseTime = purchaseTime;
    }

    public String getUsername() { return username; }
    public long getFlightId() { return flightId; }
    public LocalDateTime getPurchaseTime() { return purchaseTime; }
}
