package ru.khokhlov.biletka.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import ru.khokhlov.biletka.entity.MovieViewType;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record SessionResponseByTicket(
        Long id,
        String name,
        @JsonProperty("symbolic_name")
        String symbolicName,
        @JsonProperty("movie_type")
        MovieViewType type,
        LocalDateTime start,
        @JsonProperty("hall_number")
        Integer hallNumber,

        @JsonProperty("ticket_price")
        Integer ticketPrice
) {
}
