package com.msiepracki.beusable;

import com.msiepracki.beusable.booking.Booking;
import com.msiepracki.beusable.config.HotelRoomAllocationConfig;
import com.msiepracki.beusable.occupancy.BookingType;
import org.javamoney.moneta.Money;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.money.Monetary;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.entry;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OccupancyRevenueCalculatorTest {

    public static final String CURRENCY_EUR = "EUR";

    @InjectMocks
    private OccupancyRevenueCalculator occupancyRevenueCalculator;
    @Mock
    private HotelRoomAllocationConfig hotelRoomAllocationConfig;

    @Test
    void should_CalculateRevenues() {
        // given
        var premiumPrice = Money.of(10, CURRENCY_EUR);
        var economyPrice = Money.of(5, CURRENCY_EUR);
        var bookings = List.of(
                Booking.builder()
                        .bookingType(BookingType.ECONOMY)
                        .price(economyPrice)
                        .build(),
                Booking.builder()
                        .bookingType(BookingType.PREMIUM)
                        .price(premiumPrice)
                        .build()
        );

        when(hotelRoomAllocationConfig.getDefaultCurrency())
                .thenReturn(Monetary.getCurrency(CURRENCY_EUR));

        // when
        Map<BookingType, Money> revenuesMap = occupancyRevenueCalculator.calculateRevenues(bookings);

        // then
        assertThat(revenuesMap)
                .containsEntry(BookingType.PREMIUM, premiumPrice)
                .containsEntry(BookingType.ECONOMY, economyPrice);
    }

}