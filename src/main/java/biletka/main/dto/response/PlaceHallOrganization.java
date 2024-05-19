package biletka.main.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public record PlaceHallOrganization(
        @JsonProperty("place_id")
        Long placeId,

        @JsonProperty("place_name")
        String placeName,

        @JsonProperty("address")
        String address,

        @JsonProperty("city")
        String city,

        @JsonProperty("halls")
        HallOrganization[] hallsOrganization
) {
}
