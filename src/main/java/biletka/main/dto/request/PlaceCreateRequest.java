package biletka.main.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import org.springframework.validation.annotation.Validated;

@Validated
public record PlaceCreateRequest(
        @JsonProperty("address")
        @NotBlank(message = "Address name is mandatory!")
        String address,

        @JsonProperty("place_name")
        @NotBlank(message = "Place name is mandatory!")
        String placeName,

        @JsonProperty("city")
        @NotBlank(message = "City is mandatory!")
        String city
) {
}
