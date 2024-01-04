package ru.khokhlov.biletka.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public record TicketsMassiveResponse(
        Integer total,
        Integer price,
        @JsonProperty("on_sale")
        Integer onSale,
        Integer sales,
        @JsonProperty("generated_tickets")
        TicketsResponse[] ticketsResponses
) {
}
