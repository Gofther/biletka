package biletka.main.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record ActiveClientRequest(
        @Pattern(regexp = "\\b[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}", message = "Invalid email format")
        @NotBlank(message = "Email is mandatory!")
        String email,

        @Pattern(regexp = "\\b[A-Z0-9]{5}", message = "Invalid code format")
        @NotBlank(message = "Code is mandatory!")
        String code
) {
}
