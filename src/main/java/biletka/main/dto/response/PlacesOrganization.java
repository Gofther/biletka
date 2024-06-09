package biletka.main.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public record PlacesOrganization(
        @JsonProperty("total")
        Integer total,

        @JsonProperty("places")
        PlaceOrganization[] placesOrganization
) {
}
