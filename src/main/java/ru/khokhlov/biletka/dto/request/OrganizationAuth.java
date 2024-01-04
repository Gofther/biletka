package ru.khokhlov.biletka.dto.request;

import jakarta.validation.constraints.NotBlank;
import org.springframework.validation.annotation.Validated;

@Validated
public record OrganizationAuth(
        @NotBlank(message = "Email must not be blank")
        String email,
        @NotBlank(message = "Password id must not be blank")
        String password
) {
}
