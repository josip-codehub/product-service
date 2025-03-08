package com.ingemark.demo.products.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.ingemark.demo.products.serialization.BigDecimalCommaDeserializer;

import java.math.BigDecimal;

public record ExchangeRateResponse(
        @JsonProperty("broj_tecajnice") String exchangeRateNumber,
        @JsonProperty("datum_primjene") String applicationDate,
        @JsonProperty("drzava") String country,
        @JsonProperty("drzava_iso") String countryIso,
        @JsonProperty("kupovni_tecaj") @JsonDeserialize(using = BigDecimalCommaDeserializer.class) BigDecimal buyingRate,
        @JsonProperty("prodajni_tecaj") @JsonDeserialize(using = BigDecimalCommaDeserializer.class) BigDecimal sellingRate,
        @JsonProperty("sifra_valute") String currencyCode,
        @JsonProperty("srednji_tecaj") @JsonDeserialize(using = BigDecimalCommaDeserializer.class) BigDecimal middleRate,
        @JsonProperty("valuta") String currency
) {}
