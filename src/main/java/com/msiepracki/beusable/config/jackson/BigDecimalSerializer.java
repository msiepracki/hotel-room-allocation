package com.msiepracki.beusable.config.jackson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.math.BigDecimal;

public class BigDecimalSerializer extends JsonSerializer<BigDecimal> {
    @Override
    public void serialize(
            BigDecimal bigDecimal,
            JsonGenerator jsonGenerator,
            SerializerProvider serializerProvider
    ) throws IOException {
        if (bigDecimal.stripTrailingZeros().scale() <= 0) {
            jsonGenerator.writeNumber(bigDecimal.toBigInteger());
        } else {
            jsonGenerator.writeNumber(bigDecimal.stripTrailingZeros());
        }
    }
}
