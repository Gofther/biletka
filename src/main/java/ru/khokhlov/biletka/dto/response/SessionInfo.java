package ru.khokhlov.biletka.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public record SessionInfo(
        Long id,
        String name,
        String nameRus,
        LocalDate date,
        LocalTime time,
        String hall,
        String status,
        @JsonProperty("tickets")
        List<TicketsSessionResponse> ticketsSessionResponse
) {
}
