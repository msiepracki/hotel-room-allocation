package com.msiepracki.beusable.booking;

import com.msiepracki.beusable.money.MoneyConverter;
import lombok.RequiredArgsConstructor;
import org.javamoney.moneta.Money;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@RequiredArgsConstructor
@Component
public class BookingListConverter {

    private final MoneyConverter moneyConverter;

    public BookingList toGuestList(List<BigDecimal> moneyValues) {
        List<Booking> bookings = moneyValues.stream()
                .map(this::toGuest)
                .toList();
        return BookingList.builder()
                .bookings(bookings)
                .build();
    }

    private Booking toGuest(BigDecimal moneyValue) {
        Money money = moneyConverter.toMoney(moneyValue);
        return Booking.builder()
                .price(money)
                .build();
    }

}
