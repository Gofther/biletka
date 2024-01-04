package ru.khokhlov.biletka.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public record SessionResponse(
        Long id,
        String name,
        @JsonProperty("symbolic_name")
        String symbolicName,
        String type,
        @JsonProperty("hall_number")
        Integer hallNumber
) {
}
