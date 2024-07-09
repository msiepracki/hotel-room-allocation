package com.msiepracki.beusable.occupancy;

import lombok.Builder;
import lombok.Data;

import java.util.Objects;

@Data
@Builder
public class Occupancy {
    private BookingType bookingType;
    private int count;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Occupancy that = (Occupancy) o;
        return bookingType == that.bookingType;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(bookingType);
    }
}
