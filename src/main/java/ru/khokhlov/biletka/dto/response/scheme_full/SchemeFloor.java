package ru.khokhlov.biletka.dto.response.scheme_full;

import com.fasterxml.jackson.annotation.JsonProperty;

public record SchemeFloor(
        @JsonProperty("number")
        Integer number,
        @JsonProperty("scheme_rows")
        SchemeRow[] schemeRows
) {
}
