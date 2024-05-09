package biletka.main.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public record FavoriteResponse(
        @JsonProperty("event_id")
        Long id,

        @JsonProperty("favorite")
        Boolean favorite
) {
}
