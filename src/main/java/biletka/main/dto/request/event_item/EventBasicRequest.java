package biletka.main.dto.request.event_item;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;

@Validated
public record EventBasicRequest (
        @JsonProperty("name")
        @NotBlank(message = "Name is mandatory!")
        String name,

        @JsonProperty("name_rus")
        @NotBlank(message = "Name rus is mandatory!")
        String nameRus,

        @JsonProperty("organizaer")
        @NotBlank(message = "Organizaer is mandatory!")
        String organizaer,

        @JsonProperty("age_rating")
        @NotNull(message = "Age rating is mandatory!")
        Integer ageRating,

        @JsonProperty("type_event")
        @NotBlank(message = "Type event is mandatory!")
        String typeEvent,

        @JsonProperty("pushkin")
        @NotBlank(message = "Pushkin is mandatory!")
        Boolean pushkin,

        @JsonProperty("event_id_culture")
        Long eventIdCulture,

        @JsonProperty("show_in_poster")
        @NotBlank(message = "Show in poster is mandatory!")
        Boolean showInPoster,

        @JsonProperty("genres")
        @NotBlank(message = "Genres is mandatory!")
        String[] genres
) {
}
