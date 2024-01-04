package ru.khokhlov.biletka.dto.feign.idexchange;

public record Error(
        String error,
        String errorName,
        String userMessage
) {
}
