package ru.khokhlov.biletka.dto.request;

import jakarta.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDate;

@Validated
public record SessionFiltrRequest(
        @NotNull(message = "Date must not be null")
        LocalDate date,

        @NotNull(message = "Place name must not be null")
        String placeName
) {
}
