package biletka.main.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public record PlaceOrganization(
    @JsonProperty("id")
    Long id,

    @JsonProperty("address")
    String address,

    @JsonProperty("city")
    String city,

    @JsonProperty("place_name")
    String placeName,

    @JsonProperty("total_hall")
    Integer totalHall
) {
}
