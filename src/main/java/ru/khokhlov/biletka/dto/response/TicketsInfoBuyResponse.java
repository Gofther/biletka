package ru.khokhlov.biletka.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;

public record TicketsInfoBuyResponse(
        @JsonProperty("id_session")
        Long idSession,

        String place,

        String address,

        Integer hall,

        @JsonProperty("row_number")
        Integer rowNumber,

        @JsonProperty("seat_number")
        Integer seatNumber,

        String price,

        LocalDateTime dateTime
) {
}
