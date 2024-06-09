package biletka.main.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public record MassiveGenreResponse(
        @JsonProperty("genres")
        GenreResponse[] genreResponses
) {
}
