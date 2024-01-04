package ru.khokhlov.biletka.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record HallCreationRequestDTO(
        @NotNull
        @JsonProperty("place_id")
        Long placeId,
        @NotBlank
        String name,
        @NotNull
        Integer floor,
        @NotNull
        @JsonProperty("hall_number")
        Integer hallNumber,
        @NotNull
        @JsonProperty("seats_count")
        Integer seatsCount,
        @NotBlank
        @JsonProperty("about")
        String info,
        @NotBlank
        String file
) {
}
