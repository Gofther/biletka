package biletka.main.dto.universal;

import com.fasterxml.jackson.annotation.JsonProperty;

public record PublicEvent(
        @JsonProperty("id")
        Long id,

        @JsonProperty("name_rus")
        String nameRus,

        @JsonProperty("symbolic_name")
        String symbolicName,

        @JsonProperty("age_rating")
        Integer ageRating,

        @JsonProperty("genres")
        String[] genres,

        @JsonProperty("img")
        String img,

        @JsonProperty("type_event")
        String typeEvent,

        @JsonProperty("favorite")
        Boolean favorite,

        @JsonProperty("description")
        String description
) {
}
