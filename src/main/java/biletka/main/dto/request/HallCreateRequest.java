package biletka.main.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;

@Validated
public record HallCreateRequest(
        @NotNull(message = "Hall number is mandatory!")
        @JsonProperty("hall_number")
        Integer hallNumber,

        @NotBlank(message = "Hall name is mandatory!")
        @JsonProperty("hall_name")
        String hallName,

        @NotNull(message = "Number of seats is mandatory!")
        @JsonProperty("number_of_seats")
        Integer numberOfSeats,

        @NotBlank(message = "Info is mandatory!")
        @JsonProperty("info")
        String info,

        @NotBlank(message = "Seat group info is mandatory!")
        @JsonProperty("seat_group_info")
        String seatGroupInfo,

        @NotBlank(message = "Place id is mandatory!")
        @JsonProperty("place_id")
        Long placeId
) {
}
