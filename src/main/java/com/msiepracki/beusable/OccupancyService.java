package com.msiepracki.beusable;

import com.msiepracki.beusable.booking.Booking;
import com.msiepracki.beusable.booking.BookingList;
import com.msiepracki.beusable.occupancy.*;
import lombok.RequiredArgsConstructor;
import org.javamoney.moneta.Money;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

@RequiredArgsConstructor
@Service
public class OccupancyService {

    private final OccupancyAssignmentComponent occupancyAssignmentComponent;
    private final OccupancyRevenueCalculator occupancyRevenueCalculator;

    public OccupancyResultList calculateOccupancy(
            BookingList bookingList,
            OccupancyList occupancyList
    ) {
        int premiumRoomsAvailable = getRoomsAvailabilityByBookingType(occupancyList, BookingType.PREMIUM);
        int economyRoomsAvailable = getRoomsAvailabilityByBookingType(occupancyList, BookingType.ECONOMY);

        List<Booking> premiumBookings = occupancyAssignmentComponent.assignGuestsToBookingTypePremium(
                bookingList.getBookings(),
                premiumRoomsAvailable
        );
        int premiumRoomsLeft = premiumRoomsAvailable - premiumBookings.size();

        List<Booking> economyAndUpgradedBookings = occupancyAssignmentComponent.assignGuestsToBookingTypeEconomy(
                bookingList.getBookings(),
                economyRoomsAvailable,
                premiumRoomsLeft
        );

        List<Booking> allBookings = Stream.concat(
                premiumBookings.stream(),
                economyAndUpgradedBookings.stream()
        ).toList();

        return createOccupancyResultList(allBookings);
    }

    private OccupancyResultList createOccupancyResultList(List<Booking> allBookings) {
        Map<BookingType, Money> revenuesMapping = occupancyRevenueCalculator.calculateRevenues(allBookings);

        var occupancyResultListBuilder = OccupancyResultList.builder();
        revenuesMapping.entrySet().stream()
                .map(entry -> {
                    BookingType bookingType = entry.getKey();
                    return OccupancyResult.builder()
                            .bookingType(bookingType)
                            .revenue(entry.getValue())
                            .usage(getUsageByBookingType(allBookings, bookingType))
                            .build();
                })
                .forEach(occupancyResultListBuilder::occupancyResult);
        return occupancyResultListBuilder.build();
    }

    private int getUsageByBookingType(List<Booking> allBookings, BookingType bookingType) {
        return (int) allBookings.stream()
                .filter(booking -> bookingType.equals(booking.getBookingType()))
                .count();
    }

    private int getRoomsAvailabilityByBookingType(OccupancyList occupancyList, BookingType bookingType) {
        return occupancyList.getOccupancies().stream()
                .filter(occupancy -> occupancy.getBookingType().equals(bookingType))
                .findFirst()
                .map(Occupancy::getCount)
                .orElse(0);
    }
}