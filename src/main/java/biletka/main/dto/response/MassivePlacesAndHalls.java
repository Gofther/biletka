package biletka.main.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public record MassivePlacesAndHalls(
        @JsonProperty("places")
        PlaceHallOrganization[] placeHalls
) {
}
