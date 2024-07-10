package com.msiepracki.beusable;

import com.msiepracki.beusable.booking.Booking;
import com.msiepracki.beusable.config.HotelRoomAllocationConfig;
import com.msiepracki.beusable.occupancy.BookingType;
import lombok.RequiredArgsConstructor;
import org.javamoney.moneta.Money;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;

@RequiredArgsConstructor
@Component
class OccupancyAssignmentComponent {

    private final HotelRoomAllocationConfig hotelRoomAllocationConfig;

    List<Booking> assignGuestsToBookingTypePremium(
            List<Booking> bookingList,
            int roomsAvailable
    ) {
        return assignGuestsToBookingType(
                bookingList,
                BookingType.PREMIUM,
                roomsAvailable,
                0
        );
    }

    List<Booking> assignGuestsToBookingTypeEconomy(
            List<Booking> bookingList,
            int roomsAvailable,
            int availableForUpgrade
    ) {
        return assignGuestsToBookingType(
                bookingList,
                BookingType.ECONOMY,
                roomsAvailable,
                availableForUpgrade
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
}
