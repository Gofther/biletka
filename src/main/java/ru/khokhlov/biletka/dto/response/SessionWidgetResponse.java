package ru.khokhlov.biletka.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;
import java.time.LocalTime;

public record SessionWidgetResponse(
        Long id,
        String name,
        String symbolicName,
        LocalTime time,
        LocalDate date,
        String hall,
        @JsonProperty("left_ticket")
        Integer leftTicket,
        Integer price
) {
}
