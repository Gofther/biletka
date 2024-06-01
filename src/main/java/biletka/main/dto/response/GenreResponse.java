package biletka.main.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public record GenreResponse(
        @JsonProperty("id")
        Long id,

        @JsonProperty("genre_name")
        String genreName
) {
}
