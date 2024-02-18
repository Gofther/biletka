package ru.khokhlov.biletka.dto.response.scheme_full;

public record SchemeSeat(
        boolean occupied,
        String number,
        String group,
        String position
) {
}
