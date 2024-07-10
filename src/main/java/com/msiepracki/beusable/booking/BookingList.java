package com.msiepracki.beusable.booking;

import lombok.Builder;
import lombok.Data;
import lombok.Singular;

import java.util.List;

@Data
@Builder
public class BookingList {
    @Singular
    private List<Booking> bookings;
}
