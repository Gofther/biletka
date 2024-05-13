package biletka.main.dto.universal;

import com.fasterxml.jackson.annotation.JsonProperty;

public record MassiveSessionEvent(
        @JsonProperty("place_name")
        String placeName,

        @JsonProperty("address")
        String address,

        @JsonProperty("sessions")
        PublicSession[] publicSessions
) {
}
