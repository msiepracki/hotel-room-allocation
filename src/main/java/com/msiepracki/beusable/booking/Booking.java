package com.msiepracki.beusable.booking;

import com.msiepracki.beusable.occupancy.BookingType;
import lombok.Builder;
import lombok.Data;
import org.javamoney.moneta.Money;

@Data
@Builder
public class Booking {
    private Money price;
    private BookingType bookingType;
}
