package com.msiepracki.beusable.occupancy;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum BookingType {
    PREMIUM,
    ECONOMY(BookingType.PREMIUM);

    private BookingType upgrade;

}
