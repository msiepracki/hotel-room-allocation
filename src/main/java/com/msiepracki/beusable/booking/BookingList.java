package com.msiepracki.beusable.booking;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class BookingList {
    private List<Booking> bookings;
}
