package biletka.main.dto.universal;

import com.fasterxml.jackson.annotation.JsonProperty;

public record PublicFullInfoEvent(
        @JsonProperty("id")
        Long id,

        @JsonProperty("name_rus")
        String nameRus,

        @JsonProperty("symbolicName")
        String symbolic_name,

        @JsonProperty("description")
        String description,

        @JsonProperty("duration")
        String duration,

        @JsonProperty("rating")
        Double rating,

        @JsonProperty("age_rating")
        Integer ageRating,

        @JsonProperty("genres")
        String[] genres,

        @JsonProperty("pushkin")
        Boolean pushkin,

        @JsonProperty("img")
        String img,

        @JsonProperty("type_event")
        String typeEvent,

        @JsonProperty("writer_or_artist")
        String writerOrArtist,

        @JsonProperty("actors")
        String[] actors,

        @JsonProperty("tags")
        String[] tags,

        @JsonProperty("favorite")
        Boolean favorite,

        @JsonProperty("sessions_info")
        MassiveSessionEvent[] massiveSessionEvent
) {
}
