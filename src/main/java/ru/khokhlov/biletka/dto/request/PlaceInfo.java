package ru.khokhlov.biletka.dto.request;

import jakarta.validation.constraints.NotBlank;
import org.springframework.validation.annotation.Validated;

@Validated
public record PlaceInfo(
        @NotBlank(message = "Name must not be blank")
        String name,

        @NotBlank(message = "Address must not be blank")
        String address
) {
}
