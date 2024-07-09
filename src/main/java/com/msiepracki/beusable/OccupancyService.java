package com.msiepracki.beusable;

import com.msiepracki.beusable.booking.Booking;
import com.msiepracki.beusable.booking.BookingList;
import com.msiepracki.beusable.config.HotelRoomAllocationConfig;
import com.msiepracki.beusable.occupancy.*;
import lombok.RequiredArgsConstructor;
import org.javamoney.moneta.Money;
import org.springframework.stereotype.Service;

import javax.money.Monetary;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RequiredArgsConstructor
@Service
public class OccupancyService {

    private final HotelRoomAllocationConfig hotelRoomAllocationConfig;

    public OccupancyResultList calculateOccupancy(
            BookingList bookingList,
            OccupancyList occupancyList
    ) {
        int premiumRoomsAvailable = getRoomsAvailabilityByBookingType(occupancyList, BookingType.PREMIUM);
        int economyRoomsAvailable = getRoomsAvailabilityByBookingType(occupancyList, BookingType.ECONOMY);

        List<Booking> premiumBookings = assignGuestsToBookingType(
                bookingList.getBookings(),
                BookingType.PREMIUM,
                premiumRoomsAvailable
        );
        int premiumRoomsLeft = premiumRoomsAvailable - premiumBookings.size();

        List<Booking> economyAndUpgradedBookings = assignGuestsToBookingType(
                bookingList.getBookings(),
                BookingType.ECONOMY,
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
        Map<BookingType, Money> revenuesMapping = calculateRevenues(allBookings);

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
                .forEach(occupancyResultListBuilder::roomAvailabilityResult);
        return occupancyResultListBuilder.build();
    }

    private int getUsageByBookingType(List<Booking> allBookings, BookingType bookingType) {
        return (int) allBookings.stream()
                .filter(booking -> bookingType.equals(booking.getBookingType()))
                .count();
    }

    private Map<BookingType, Money> calculateRevenues(List<Booking> bookings) {
        Money zeroMoney = Money.zero(Monetary.getCurrency(hotelRoomAllocationConfig.getDefaultCurrency()));
        return bookings.stream()
                .collect(
                        Collectors.groupingBy(
                                Booking::getBookingType,
                                Collectors.reducing(zeroMoney, Booking::getPrice, Money::add)
                        )
                );
    }

    private List<Booking> assignGuestsToBookingType(
            List<Booking> bookingList,
            BookingType bookingType,
            int roomsAvailable
    ) {
        return assignGuestsToBookingType(
                bookingList,
                bookingType,
                roomsAvailable,
                0
        );
    }

    private List<Booking> assignGuestsToBookingType(
            List<Booking> bookingList,
            BookingType bookingType,
            int roomsAvailable,
            int availableForUpgrade
    ) {
        List<Booking> guestsWithAvailableForUpgrade = bookingList.stream()
                .filter(getGuestFilterByBookingType(bookingType))
                .sorted(Comparator.comparing(Booking::getPrice).reversed())
                .limit(roomsAvailable + availableForUpgrade)
                .peek(booking -> booking.setBookingType(bookingType))
                .toList();
        if (roomsAvailable < guestsWithAvailableForUpgrade.size()) {
            int guestsCountToUpgrade = guestsWithAvailableForUpgrade.size() - roomsAvailable;
            int amountOfGuestsToUpgrade = Math.max(0, Math.min(guestsCountToUpgrade, availableForUpgrade));
            upgradeBookings(amountOfGuestsToUpgrade, guestsWithAvailableForUpgrade);
        }
        return guestsWithAvailableForUpgrade;
    }

    private void upgradeBookings(int availableForUpgrade, List<Booking> guestsWithAvailableForUpgrade) {
        guestsWithAvailableForUpgrade.stream()
                .limit(availableForUpgrade)
                .forEach(booking -> {
                    BookingType upgradedBookingType = booking.getBookingType().getUpgrade();
                    booking.setBookingType(upgradedBookingType);
                });
    }

    private Predicate<Booking> getGuestFilterByBookingType(BookingType bookingType) {
        Money premiumThresholdMoney = hotelRoomAllocationConfig.getPremiumThresholdMoney();
        return switch (bookingType) {
            case PREMIUM -> booking -> booking.getPrice().isGreaterThanOrEqualTo(premiumThresholdMoney);
            case ECONOMY -> booking -> booking.getPrice().isLessThan(premiumThresholdMoney);
        };
    }

    private int getRoomsAvailabilityByBookingType(OccupancyList occupancyList, BookingType bookingType) {
        return occupancyList.getOccupancies().stream()
                .filter(occupancy -> occupancy.getBookingType().equals(bookingType))
                .findFirst()
                .map(Occupancy::getCount)
                .orElse(0);
    }
}