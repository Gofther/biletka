package ru.khokhlov.biletka.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;

public record TicketEditInfo(
        @NotNull(message = "Session id must not be null")
        Long id,
        @NotNull(message = "Price must not be null")
        Integer price,
        @NotNull(message = "Sales must not be null")
        Integer sales,
        @NotNull(message = "On sale must not be null")
        @JsonProperty("on_sale")
        Integer onSale
) {
}
