package biletka.main.dto.response.TotalSession;

import com.fasterxml.jackson.annotation.JsonProperty;

public record PlacesByOrganization(
        @JsonProperty("place_name")
        String placeName,

        @JsonProperty("city_name")
        String cityName,

        @JsonProperty("place_address")
        String placeAddress,

        @JsonProperty("events")
        EventsByPlace[] eventsByPlace
) {
}
