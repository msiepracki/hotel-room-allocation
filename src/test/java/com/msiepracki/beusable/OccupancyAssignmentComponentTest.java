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
import static org.assertj.core.api.Assertions.tuple;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OccupancyAssignmentComponentTest {

    public static final String CURRENCY_EUR = "EUR";

    @InjectMocks
    private OccupancyAssignmentComponent occupancyAssignmentComponent;
    @Mock
    private HotelRoomAllocationConfig hotelRoomAllocationConfig;

    @Test
    void should_AssignPremiumBookings() {
        // given
        var roomsAvailable = 2;
        List<Booking> unassignedBookings = getUnassignedBookings();

        when(hotelRoomAllocationConfig.getPremiumThresholdMoney())
                .thenReturn(getEuro(10));

        // when
        List<Booking> assignedBookings = occupancyAssignmentComponent.assignGuestsToBookingTypePremium(
                unassignedBookings,
                roomsAvailable
        );

        // then
        assertThat(assignedBookings)
                .hasSize(roomsAvailable)
                .extracting(
                        Booking::getBookingType,
                        booking -> booking.getPrice().getNumber().intValueExact()
                ).containsExactlyInAnyOrder(
                        tuple(BookingType.PREMIUM, 100),
                        tuple(BookingType.PREMIUM, 23)
                );
    }

    @Test
    void should_AssignEconomyBookings() {
        // given
        var roomsAvailable = 2;
        List<Booking> unassignedBookings = getUnassignedBookings();

        when(hotelRoomAllocationConfig.getPremiumThresholdMoney())
                .thenReturn(getEuro(10));

        // when
        List<Booking> assignedBookings = occupancyAssignmentComponent.assignGuestsToBookingTypeEconomy(
                unassignedBookings,
                roomsAvailable,
                0
        );

        // then
        assertThat(assignedBookings)
                .hasSize(roomsAvailable)
                .extracting(
                        Booking::getBookingType,
                        booking -> booking.getPrice().getNumber().intValueExact()
                ).containsExactlyInAnyOrder(
                        tuple(BookingType.ECONOMY, 8),
                        tuple(BookingType.ECONOMY, 4)
                );
    }

    @Test
    void should_AssignEconomyBookings_WithUpgradeAvailable() {
        // given
        var roomsAvailable = 2;
        var upgradesAvailable = 1;
        List<Booking> unassignedBookings = getUnassignedBookings();

        when(hotelRoomAllocationConfig.getPremiumThresholdMoney())
                .thenReturn(getEuro(10));

        // when
        List<Booking> assignedBookings = occupancyAssignmentComponent.assignGuestsToBookingTypeEconomy(
                unassignedBookings,
                roomsAvailable,
                upgradesAvailable
        );

        // then
        assertThat(assignedBookings)
                .hasSize(roomsAvailable + upgradesAvailable)
                .extracting(
                        Booking::getBookingType,
                        booking -> booking.getPrice().getNumber().intValueExact()
                ).containsExactlyInAnyOrder(
                        tuple(BookingType.PREMIUM, 8),
                        tuple(BookingType.ECONOMY, 4),
                        tuple(BookingType.ECONOMY, 3)
                );
    }

    private List<Booking> getUnassignedBookings() {
        return List.of(
                getUnassignedBooking(2),
                getUnassignedBooking(3),
                getUnassignedBooking(4),
                getUnassignedBooking(8),
                getUnassignedBooking(10),
                getUnassignedBooking(23),
                getUnassignedBooking(100)
        );
    }

    private Booking getUnassignedBooking(int value) {
        return Booking.builder().price(getEuro(value)).build();
    }

    private Money getEuro(int value) {
        return Money.of(value, CURRENCY_EUR);
    }

}