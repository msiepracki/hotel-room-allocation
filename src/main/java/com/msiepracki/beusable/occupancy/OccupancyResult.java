package com.msiepracki.beusable.occupancy;

import lombok.Builder;
import lombok.Data;
import org.javamoney.moneta.Money;

@Data
@Builder
public class OccupancyResult {
    private BookingType bookingType;
    private int usage;
    private Money revenue;
}
