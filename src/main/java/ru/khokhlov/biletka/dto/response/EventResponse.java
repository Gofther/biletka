package ru.khokhlov.biletka.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public record EventResponse(
        Long id,
        @JsonProperty("symbolic_name")
        String symbolicName
) {
}
