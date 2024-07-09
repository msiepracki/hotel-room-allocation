package com.msiepracki.beusable.occupancy.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record OccupancyResponseDto(
        @NotNull
        @PositiveOrZero
        int usagePremium,
        @NotNull
        @PositiveOrZero
        BigDecimal revenuePremium,
        @NotNull
        @PositiveOrZero
        int usageEconomy,
        @NotNull
        @PositiveOrZero
        BigDecimal revenueEconomy
) {

}
