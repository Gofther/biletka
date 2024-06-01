package biletka.main.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public record MassiveTypeResponse(
        @JsonProperty("types")
        TypeResponse[] typeResponses
) {
}
