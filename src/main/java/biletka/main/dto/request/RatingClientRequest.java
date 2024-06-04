package biletka.main.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record RatingClientRequest(
        @Pattern(regexp = "^\\d+[A-Za-z0-9._%+@-]+$", message = "Invalid event format")
        @JsonProperty("event")
        @NotBlank(message = "Event is mandatory!")
        String eventSymbolic,

        @JsonProperty("rating_client")
        @NotNull(message = "Rating is mandatory!")
        Double rating
) {
}
