package ru.khokhlov.biletka.dto.response;

import java.time.LocalDateTime;

public record DeleteSession(
        String name,
        LocalDateTime dateTime
) {
}
