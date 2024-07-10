package com.msiepracki.beusable.occupancy;

import com.msiepracki.beusable.occupancy.dto.OccupancyRequestDto;
import com.msiepracki.beusable.occupancy.dto.OccupancyResponseDto;
import org.javamoney.moneta.Money;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.groups.Tuple.tuple;

class OccupancyListConverterTest {

    private final OccupancyListConverter occupancyListConverter = new OccupancyListConverter();

    @Test
    void should_ConvertOccupancyRequestToOccupancyList() {
        // given
        var occupancyRequestDto = OccupancyRequestDto.builder()
                .premiumRooms(3)
                .economyRooms(2)
                .build();

        // when
        OccupancyList occupancyList = occupancyListConverter.toOccupancyList(occupancyRequestDto);

        // then
        assertThat(occupancyList.getOccupancies())
                .extracting(
                        Occupancy::getBookingType,
                        Occupancy::getCount
                )
                .containsExactlyInAnyOrder(
                        tuple(BookingType.PREMIUM, 3),
                        tuple(BookingType.ECONOMY, 2)
                );
    }
    @Test
    void should_ConvertOccupancyResultListToOccupancyResponse() {
        // given
        var occupancyResultList = OccupancyResultList.builder()
                .occupancyResult(
                        OccupancyResult.builder()
                                .bookingType(BookingType.PREMIUM)
                                .usage(4)
                                .revenue(Money.of(815.15, "USD"))
                                .build())
                .occupancyResult(
                        OccupancyResult.builder()
                                .bookingType(BookingType.ECONOMY)
                                .usage(23)
                                .revenue(Money.of(420.00, "USD"))
                                .build()
                )
                .build();

        // when
        OccupancyResponseDto occupancyResponse = occupancyListConverter.toOccupancyResponseDto(occupancyResultList);

        // then
        assertThat(occupancyResponse)
                .extracting(
                        OccupancyResponseDto::usagePremium,
                        OccupancyResponseDto::revenuePremium,
                        OccupancyResponseDto::usageEconomy,
                        OccupancyResponseDto::revenueEconomy
                )
                .usingComparatorForType(BigDecimal::compareTo, BigDecimal.class)
                .containsExactlyInAnyOrder(
                        4,
                        new BigDecimal("815.15"),
                        23,
                        new BigDecimal("420")
                );
    }

}
