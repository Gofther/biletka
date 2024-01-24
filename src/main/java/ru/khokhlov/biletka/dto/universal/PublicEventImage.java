package ru.khokhlov.biletka.dto.universal;

public record PublicEventImage(
        byte[] imageData,
        String name,
        String type
) {
}
