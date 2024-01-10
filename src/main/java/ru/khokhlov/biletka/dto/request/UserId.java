package ru.khokhlov.biletka.dto.request;

import jakarta.validation.constraints.NotNull;

public record UserId(
        @NotNull(message = "Id must not be null")
        Long id
) {
}
