package biletka.main.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDateTime;

@Validated
public record SessionCreateRequest(
        @JsonProperty("start_time")
        LocalDateTime startTime,

        @JsonProperty("price")
        Double price,

        @JsonProperty("event_id")
        Long eventId,

        @JsonProperty("hall_id")
        Long hallId,

        @JsonProperty("type_of_movie")
        String typeOfMovie
) {
}
