package ru.khokhlov.biletka.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import org.springframework.validation.annotation.Validated;

import java.util.Date;

@Validated
public record ClientRegistration(
        @Pattern(regexp = "\\b[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Z|a-z]{2,}\\b", message = "Invalid email format")
        @NotBlank(message = "Email is mandatory")
        String email,

        @Pattern(regexp = "^[A-Za-zА-Яа-я]+\\s+[A-Za-zА-Яа-я]+\\s+[A-Za-zА-Яа-я]+$", message = "Invalid full name format")
        String fullName,

        @Pattern(regexp = "^7\\d{10}$", message = "Invalid Russian phone number format 79876543210")
        String phoneNumber,

        @Pattern(regexp = "^(?=.*[a-zа-я])(?=.*[A-ZА-Я])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$", message = "Invalid password format")
        @NotBlank(message = "Password is mandatory")
        String password,

        @NotNull(message = "Birthdate cannot be null")
        @Past(message = "Birthdate must be a past date")
        Date birthday
) {
}
