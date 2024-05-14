package biletka.main.dto.universal;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;
import java.util.Date;

public record PublicSession(
        @JsonProperty("id")
        Long id,
        @JsonProperty("price")
        Double price,

        @JsonProperty("start_time")
        LocalDateTime startTime,

        @JsonProperty("message_status")
        Boolean message
) {
}
