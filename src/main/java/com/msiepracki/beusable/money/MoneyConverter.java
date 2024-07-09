package com.msiepracki.beusable.money;

import com.msiepracki.beusable.config.HotelRoomAllocationConfig;
import lombok.RequiredArgsConstructor;
import org.javamoney.moneta.Money;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@RequiredArgsConstructor
@Component
public class MoneyConverter {

    private final HotelRoomAllocationConfig hotelRoomAllocationConfig;

    public Money toMoney(BigDecimal moneyValue) {
        return Money.of(
                moneyValue,
                hotelRoomAllocationConfig.getDefaultCurrency()
        );
    }
}
