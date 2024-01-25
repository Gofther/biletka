package ru.khokhlov.biletka.dto.request.event_full_ei;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;

@Validated
public record BasicInformationRequest(
        @NotBlank(message = "Name must not be null")
        String name,

        @NotBlank(message = "Name rus must not be null")
        @JsonProperty("name_rus")
        String nameRus,

        @NotBlank(message = "Event type must not be null")
        @JsonProperty("event_type")
        String eventType,

        @NotEmpty
        String[] genre,

        @Min(value = 0)
        @JsonProperty("age_rating")
        Integer ageRating,

        @NotBlank(message = "Organizer must not be null")
        String organizer,

        @NotNull(message = "Pushkin must not be null")
        Boolean pushkin,

        @NotNull(message = "Event id culture must not be null")
        @JsonProperty("event_id_culture")
        Long eventIDCulture,

        @NotNull(message = "Image id must not be null")
        Long img_id,

        @NotNull(message = "Show in poster must be not null")
        @JsonProperty("show_in_poster")
        Boolean showInPoster
) {
}
