package ru.khokhlov.biletka.dto.response.scheme_full;

import com.fasterxml.jackson.annotation.JsonProperty;

public record SchemeRow(
        @JsonProperty("row_number")
        String rowNumber,
        @JsonProperty("scheme_seats")
        SchemeSeat[] schemeSeats
) {
}
