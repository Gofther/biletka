package ru.khokhlov.biletka.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record PlacesAndInfoSession(
        Long id,
        @JsonProperty(value = "filial_name")
        String filialName,
        String address,
        String[] timing
) {
}
