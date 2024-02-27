package ru.khokhlov.biletka.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record TicketsInfoBuy(
        @NotNull(message = "Id session must not null")
        @JsonProperty("session_id")
        Long id,

        @NotNull(message = "Row number must not null")
        Integer row,

        @NotNull(message = "Seat number must not null")
        Integer seat
) {
}
