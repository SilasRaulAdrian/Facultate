package org.example.domain;

import java.time.LocalDate;

public class SpecialOffer {
    private final long specialOfferId;
    private final long hotelId;
    private final LocalDate startDate;
    private final LocalDate endDate;
    private final int percents;

    public SpecialOffer(long specialOfferId, long hotelId, LocalDate startDate, LocalDate endDate, int percents) {
        this.specialOfferId = specialOfferId;
        this.hotelId = hotelId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.percents = percents;
    }

    public long getSpecialOfferId() { return specialOfferId; }
    public long getHotelId() { return hotelId; }
    public LocalDate getStartDate() { return startDate; }
    public LocalDate getEndDate() { return endDate; }
    public int getPercents() { return percents; }
}
