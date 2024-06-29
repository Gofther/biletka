package biletka.main.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;

public record ClientTicketResponse(
        @NotNull
        @JsonProperty("event_name")
        String eventName,
        @NotNull
        @JsonProperty("city")
        String city,
        @NotNull
        @JsonProperty("address")
        String address,
        @NotNull
        @JsonProperty("place")
        String place,
        @NotNull
        @JsonProperty("hall")
        String hall,
        @NotNull
        @JsonProperty("date")
        String date,
        @NotNull
        @JsonProperty("time")
        String time
) {
}
