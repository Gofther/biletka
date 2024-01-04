package ru.khokhlov.biletka.dto.request;

import jakarta.validation.constraints.NotBlank;
import org.springframework.validation.annotation.Validated;

@Validated
public record EventSymbolicName(
        @NotBlank(message = "Symbolic name cannot be blank!")
        String symbolicName
) {
}
