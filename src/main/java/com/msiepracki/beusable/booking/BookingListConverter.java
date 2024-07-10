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

    public BookingList toBookingList(List<BigDecimal> moneyValues) {
        var bookingListBuilder = BookingList.builder();
        moneyValues.stream()
                .map(this::toBooking)
                .forEach(bookingListBuilder::booking);
        return bookingListBuilder.build();
    }

    private Booking toBooking(BigDecimal moneyValue) {
        Money money = moneyConverter.toMoney(moneyValue);
        return Booking.builder()
                .price(money)
                .build();
    }

}
