package ru.khokhlov.biletka.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

public record ExitResponse(
        @Schema(description = "User email")
        String email
) {
}
