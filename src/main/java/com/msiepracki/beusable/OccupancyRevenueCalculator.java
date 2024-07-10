package com.msiepracki.beusable;

import com.msiepracki.beusable.booking.Booking;
import com.msiepracki.beusable.config.HotelRoomAllocationConfig;
import com.msiepracki.beusable.occupancy.*;
import lombok.RequiredArgsConstructor;
import org.javamoney.moneta.Money;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
class OccupancyRevenueCalculator {

    private final HotelRoomAllocationConfig hotelRoomAllocationConfig;

    Map<BookingType, Money> calculateRevenues(List<Booking> bookings) {
        return bookings.stream()
                .collect(getBookingMapCollector());
    }

    private Collector<Booking, ?, Map<BookingType, Money>> getBookingMapCollector() {
        return Collectors.groupingBy(
                Booking::getBookingType,
                Collectors.reducing(getZeroMoney(), Booking::getPrice, Money::add)
        );
    }

    private Money getZeroMoney() {
        return Money.zero(hotelRoomAllocationConfig.getDefaultCurrency());
    }

}