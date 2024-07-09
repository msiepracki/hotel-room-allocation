package com.msiepracki.beusable.occupancy.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Builder;
import lombok.Singular;

import java.math.BigDecimal;
import java.util.List;

@Builder
public record OccupancyRequestDto(
        @NotNull
        @PositiveOrZero
        int premiumRooms,
        @NotNull
        @PositiveOrZero
        int economyRooms,
        @NotNull
        @Singular
        @Valid
        List<@Positive BigDecimal> potentialGuests
) {

}
