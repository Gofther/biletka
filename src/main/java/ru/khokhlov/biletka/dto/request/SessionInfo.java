package ru.khokhlov.biletka.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDateTime;

@Validated
public record SessionInfo(
        @NotNull(message = "Start timestamp must not be null")
        LocalDateTime datetime,

        @NotBlank(message = "Movie view type must not be blank")
        @JsonProperty("movie_type")
        String typeOfMovie,

        @NotBlank(message = "Event must not be blank")
        @JsonProperty("event_name")
        String eventSymbolicName,

        @NotBlank(message = "Event type must not be blank")
        @JsonProperty("event_type")
        String eventType,

        @NotNull(message = "Hall scheme must not be null")
        @JsonProperty("hall_id")
        Long hallSchemeId
) {
}
