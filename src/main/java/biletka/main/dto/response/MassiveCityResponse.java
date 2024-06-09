package biletka.main.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public record MassiveCityResponse(
        @JsonProperty("cities")
        CityResponse[] cityResponses
) {
}
