package ru.khokhlov.biletka.dto.universal;

public record PublicEvent(
        Long id,
        String name,
        String symbolicName,
        String nameRus,
        String type,
        String duration,
        Boolean pushkin,
        Boolean showInPoster,
        String imgFile
) {
}
