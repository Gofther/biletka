package biletka.main.dto.request.event_item;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import org.springframework.validation.annotation.Validated;

@Validated
public record EventAdditional(
        @JsonProperty("author")
        @NotBlank(message = "Author is mandatory!")
        String author,

        @JsonProperty("director")
        @NotBlank(message = "Director is mandatory!")
        String director,

        @JsonProperty("writer_or_artist")
        @NotBlank(message = "Writer or artist is mandatory!")
        String writerOrArtist,

        @JsonProperty("actors")
        @NotBlank(message = "Actors is mandatory!")
        String[] actors,

        @JsonProperty("tags")
        @NotBlank(message = "Tags is mandatory!")
        String[] tags
) {
}
