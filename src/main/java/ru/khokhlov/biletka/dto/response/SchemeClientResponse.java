package ru.khokhlov.biletka.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import ru.khokhlov.biletka.dto.response.scheme_full.SchemeFloor;

public record SchemeClientResponse(
        @JsonProperty("scheme_floors")
        SchemeFloor[] schemeFloors,

        String[] group,

        Integer price
) {
}
