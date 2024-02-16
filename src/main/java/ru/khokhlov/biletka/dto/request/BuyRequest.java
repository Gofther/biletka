package ru.khokhlov.biletka.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import org.springframework.validation.annotation.Validated;

@Validated
public record BuyRequest(
        @NotNull(message = "Id session must not null")
        Long idSession,

        @NotBlank(message = "Full name must not be blank")
        @Pattern(regexp = "^[A-Za-zА-Яа-я]+\\s+[A-Za-zА-Яа-я]+\\s+[A-Za-zА-Яа-я]+$", message = "Invalid full name format")
        String fullName,

        @NotBlank(message = "Mail must not be blank")
        @Pattern(regexp = "\\b[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Z|a-z]{2,}\\b", message = "Invalid email format")
        String email,

        @NotBlank(message = "Mail must not be blank")
        @Pattern(regexp = "^7\\d{10}$", message = "Invalid Russian phone number format 79876543210")
        String tel,

        @NotNull(message = "row number must not null")
        Integer rawNumber,

        @NotNull(message = "Seat number must not null")
        Integer seatNumber,

        Long idUser
) {
}
