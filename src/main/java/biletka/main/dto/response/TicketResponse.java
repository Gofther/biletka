package biletka.main.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;

public record TicketResponse(
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
        @JsonProperty("row")
        Integer row,
        @JsonProperty("seat")
        Integer seat,
        @NotNull
        @JsonProperty("date")
        String date,
        @NotNull
        @JsonProperty("time")
        String time,
        @JsonProperty("email")
        String email,
        @NotNull
        @JsonProperty("phone")
        String phone,
        @NotNull
        @JsonProperty("full_name")
        String fullName,
        @NotNull
        @JsonProperty("qr_code")
        String qrCode
) {
}
