package ru.khokhlov.biletka.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public record TicketsResponse(
        Long id,
        Integer price,
        @JsonProperty("on_sale")
        Integer onSale,
        Integer sales
) {
}
