package com.ingemark.demo.products.serialization;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Optional;

public class BigDecimalCommaDeserializer extends JsonDeserializer<BigDecimal> {
    @Override
    public BigDecimal deserialize(
            final JsonParser p,
            final DeserializationContext context
    ) throws IOException {
        return Optional
                .ofNullable(p.getText())
                .map(s -> s.replace(",", "."))
                .map(BigDecimal::new)
                .orElse(null);
    }
}
