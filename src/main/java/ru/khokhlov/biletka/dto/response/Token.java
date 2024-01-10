package ru.khokhlov.biletka.dto.response;

public record Token(
        Long id,
        String role,
        String type,
        String accessToken
) {
}
