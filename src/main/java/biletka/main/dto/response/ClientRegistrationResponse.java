package biletka.main.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ClientRegistrationResponse(
        @JsonProperty("message")
        String message
) {
}
