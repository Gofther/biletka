package biletka.main.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDateTime;

@Validated
public record SessionCreateRequest(
        @JsonProperty("start_time")
        @NotNull(message = "Start time is mandatory!")
        LocalDateTime startTime,

        @JsonProperty("price")
        @NotNull(message = "Price is mandatory!")
        Double price,

        @JsonProperty("event_id")
        @NotNull(message = "Event id is mandatory!")
        Long eventId,

        @JsonProperty("hall_id")
        @NotNull(message = "Hall is mandatory!")
        Long hallId,

        @JsonProperty("type_of_movie")
        @NotBlank(message = "Type of movie is mandatory!")
        String typeOfMovie
) {
}
