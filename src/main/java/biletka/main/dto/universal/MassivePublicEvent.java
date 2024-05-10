package biletka.main.dto.universal;

import com.fasterxml.jackson.annotation.JsonProperty;

public record MassivePublicEvent(
        @JsonProperty("public_events")
        PublicEvent[] publicEvents
) {
}
