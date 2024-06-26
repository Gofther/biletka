package biletka.main.dto.response.TotalSession;

import com.fasterxml.jackson.annotation.JsonProperty;

public record EventsByPlace(
        @JsonProperty("event_name")
        String eventName,

        @JsonProperty("type")
        String type,

        @JsonProperty("sessions")
        SessionResponse[] sessions
) {
}
