package biletka.main.dto.response;

import biletka.main.entity.Session;
import com.fasterxml.jackson.annotation.JsonProperty;

public record PlacesSessionsResponse(
        @JsonProperty("place")
        String place,
        @JsonProperty("sessions_amount")
        Integer sessionsAmount,
        @JsonProperty("sessions_percent")
        Double sessions_percent
) {
}
