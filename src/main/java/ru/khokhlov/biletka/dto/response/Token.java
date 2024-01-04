package ru.khokhlov.biletka.dto.response;

public record Token(
        Long id,
        String type,
        String accessToken
) {
}
