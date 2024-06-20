package biletka.main.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public record BuyTicketResponse(
        @JsonProperty("message")
        String message,
        @JsonProperty("url")
        String url
) {
}
