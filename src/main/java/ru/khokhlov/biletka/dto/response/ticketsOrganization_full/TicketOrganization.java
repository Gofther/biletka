package ru.khokhlov.biletka.dto.response.ticketsOrganization_full;

import com.fasterxml.jackson.annotation.JsonProperty;

public record TicketOrganization(
        Long id,

        @JsonProperty("full_name")
        String fullName,
        @JsonProperty("date_time")
        String dateTime,

        @JsonProperty("row_number")
        Integer rowNumber,

        @JsonProperty("seat_number")
        Integer seatNumber,

        Boolean status
) {
}
