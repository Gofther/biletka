package biletka.main.dto.response;


import com.fasterxml.jackson.annotation.JsonProperty;

public record MessageCreateResponse(
        @JsonProperty("message")
        String message
) {
}
