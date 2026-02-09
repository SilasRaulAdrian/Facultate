package org.example.domain;

import java.time.LocalDate;

public record ClientOfferRow(String hotelName,
                             String locationName,
                             LocalDate startDate,
                             LocalDate endDate,
                             int percents) {
}
