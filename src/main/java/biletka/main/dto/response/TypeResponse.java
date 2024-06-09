package biletka.main.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public record TypeResponse(
        @JsonProperty("id")
        Long id,

        @JsonProperty("type_name")
        String typeName
) {
}
