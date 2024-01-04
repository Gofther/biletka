package ru.khokhlov.biletka.dto.universal;

import java.time.LocalDateTime;

public record PublicSession(
        Long id,
        String name,
        String symbolicName,
        LocalDateTime date,
        Integer issued,
        String status
) {
}
