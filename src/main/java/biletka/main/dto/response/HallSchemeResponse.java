package biletka.main.dto.response;

import biletka.main.dto.response.HallScheme.SchemeFloor;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;

public record HallSchemeResponse(
        @NotNull
        @JsonProperty("scheme_floors")
        SchemeFloor[] schemeFloors
) {
}
