package com.msiepracki.beusable.booking;

import com.msiepracki.beusable.config.HotelRoomAllocationConfig;
import com.msiepracki.beusable.money.MoneyConverter;
import org.javamoney.moneta.Money;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import javax.money.Monetary;
import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookingListConverterTest {

    private final String CURRENCY_EUR = "EUR";

    @InjectMocks
    private BookingListConverter bookingListConverter;
    @Mock
    private HotelRoomAllocationConfig hotelRoomAllocationConfig;
    @Mock
    private MoneyConverter moneyConverter;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(
                moneyConverter,
                "hotelRoomAllocationConfig",
                hotelRoomAllocationConfig
        );
        when(hotelRoomAllocationConfig.getDefaultCurrency())
                .thenReturn(Monetary.getCurrency(CURRENCY_EUR));
    }

    @Test
    void should_ConvertMoneyValuesToBookingList() {
        // given
        var moneyValues = List.of(
                new BigDecimal(11),
                new BigDecimal("12.0"),
                BigDecimal.ONE
        );

        when(moneyConverter.toMoney(any(BigDecimal.class)))
                .thenCallRealMethod();

        // when
        BookingList bookingList = bookingListConverter.toBookingList(moneyValues);

        // then
        assertThat(bookingList.getBookings())
                .extracting(Booking::getPrice)
                .containsExactlyInAnyOrder(
                        Money.of(new BigDecimal("11"), CURRENCY_EUR),
                        Money.of(new BigDecimal("12"), CURRENCY_EUR),
                        Money.of(new BigDecimal("1"), CURRENCY_EUR)
                );
    }

}
