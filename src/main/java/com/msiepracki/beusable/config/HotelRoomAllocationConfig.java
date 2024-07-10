package com.msiepracki.beusable.config;

import lombok.Setter;
import org.javamoney.moneta.Money;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import javax.money.CurrencyUnit;
import javax.money.Monetary;
import java.math.BigDecimal;

@Setter
@Configuration
@ConfigurationProperties(prefix = "hotel-room-allocation-config")
public class HotelRoomAllocationConfig {
    private String defaultCurrency;
    private BigDecimal premiumThreshold;

    public Money getPremiumThresholdMoney() {
        return Money.of(premiumThreshold, defaultCurrency);
    }
    public CurrencyUnit getDefaultCurrency() {
        return Monetary.getCurrency(defaultCurrency);
    }
}
