package ru.khokhlov.biletka.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public record TicketsSessionResponse(
        Long ticketId,
        Integer total,
        Integer price,
        @JsonProperty("on_sale")
        Integer onSale,
        Integer sales
) {
}
