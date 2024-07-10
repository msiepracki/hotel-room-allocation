package com.msiepracki.beusable.money;

import com.msiepracki.beusable.config.HotelRoomAllocationConfig;
import org.javamoney.moneta.Money;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.money.Monetary;
import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MoneyConverterTest {

    @InjectMocks
    private MoneyConverter moneyConverter;
    @Mock
    private HotelRoomAllocationConfig hotelRoomAllocationConfig;

    @Test
    void should_ConvertBigDecimalToMoney() {
        // given
        var moneyString = "100";
        var source = new BigDecimal(moneyString);

        var expectedCurrency = "EUR";
        when(hotelRoomAllocationConfig.getDefaultCurrency())
                .thenReturn(Monetary.getCurrency(expectedCurrency));

        // when
        Money target = moneyConverter.toMoney(source);

        // then
        assertThat(target)
                .extracting(
                        money -> money.getNumberStripped().toPlainString(),
                        money -> money.getCurrency().getCurrencyCode()
                )
                .containsExactly(
                        moneyString,
                        expectedCurrency
                );
    }

}
