package ru.khokhlov.biletka.dto.response;

import java.time.LocalDate;

public record DatesOfEvents(
        String[] dates
) {
}
