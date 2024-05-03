package biletka.main.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import org.springframework.validation.annotation.Validated;

import java.util.Date;

@Validated
public record ClientRegistrationRequest(
        @Pattern(regexp = "\\b[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}", message = "Invalid email format")
        @NotBlank(message = "Email is mandatory!")
        String email,

        @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,25}$", message = "Invalid password email")
        @NotBlank(message = "Password is mandatory!")
        String password,
        @Pattern(regexp = "^[A-Za-zА-Яа-я]+\\s+[A-Za-zА-Яа-я]+\\s+[A-Za-zА-Яа-я]+$", message = "Invalid full name format")
        @JsonProperty("full_name")
        String fullName,

        @Pattern(regexp = "^7\\d{10}$", message = "Invalid Russian phone number format 79876543210")
        @JsonProperty("phone_number")
        String phoneNumber,

        @NotNull(message = "Birthdate cannot be null")
        @Past(message = "Birthdate must be a past date")
        Date birthday
) {
}
