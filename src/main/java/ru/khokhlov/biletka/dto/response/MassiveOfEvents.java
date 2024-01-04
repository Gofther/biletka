package ru.khokhlov.biletka.dto.response;

import ru.khokhlov.biletka.dto.universal.PublicEvent;

public record MassiveOfEvents(
        PublicEvent[] events
) {
}
