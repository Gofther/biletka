package biletka.main.dto.response;

import biletka.main.entity.Genre;
import biletka.main.entity.Tag;
import com.fasterxml.jackson.annotation.JsonProperty;

public record EventOrganization(
    @JsonProperty("id")
    Long id,

    @JsonProperty("name")
    String name,

    @JsonProperty("symbolic_name")
    String symbolicName,

    @JsonProperty("rating")
    Double rating,

    @JsonProperty("duration")
    String duration,

    @JsonProperty("pushkin")
    Boolean pushkin,

    @JsonProperty("tags")
    Tag[] tags,

    @JsonProperty("genres")
    Genre[] genres,

    @JsonProperty("age_rating")
    String ageRating,

    @JsonProperty("total_sessions")
    Integer totalSessions
) {
}
