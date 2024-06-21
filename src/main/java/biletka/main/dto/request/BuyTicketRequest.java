package biletka.main.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Pattern;

public record BuyTicketRequest(
        @Pattern(regexp = "\\b[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}", message = "Invalid email format")
        @JsonProperty("email")
        String email,
        @Pattern(regexp = "^[A-Za-zА-Яа-я]+\\s+[A-Za-zА-Яа-я]+\\s+[A-Za-zА-Яа-я]+$", message = "Invalid full name format")
        @JsonProperty("full_name")
        String fullName,
        @Pattern(regexp = "^7\\d{10}$", message = "Invalid Russian phone number format 79876543210")
        @JsonProperty("phone")
        String phone,
        @JsonProperty("session_id")
        Long sessionId,
        @JsonProperty("seat_number")
        Integer seatNumber,
        @JsonProperty("row_number")
        Integer rowNumber

) {
}
