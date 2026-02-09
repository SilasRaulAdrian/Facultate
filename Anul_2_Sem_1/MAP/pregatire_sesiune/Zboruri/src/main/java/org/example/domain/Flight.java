package org.example.domain;

import java.time.LocalDateTime;

public class Flight {
    private final long flightId;
    private final String from;
    private final String to;
    private final LocalDateTime departureTime;
    private final LocalDateTime landingTime;
    private final int seats;

    public Flight(long flightId, String from, String to,
                  LocalDateTime departureTime, LocalDateTime landingTime, int seats) {
        this.flightId = flightId;
        this.from = from;
        this.to = to;
        this.departureTime = departureTime;
        this.landingTime = landingTime;
        this.seats = seats;
    }

    public long getFlightId() { return flightId; }
    public String getFrom() { return from; }
    public String getTo() { return to; }
    public LocalDateTime getDepartureTime() { return departureTime; }
    public LocalDateTime getLandingTime() { return landingTime; }
    public int getSeats() { return seats; }
}
