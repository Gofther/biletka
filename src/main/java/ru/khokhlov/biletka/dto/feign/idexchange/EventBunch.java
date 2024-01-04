package ru.khokhlov.biletka.dto.feign.idexchange;

import ru.khokhlov.biletka.enums.Status;

public record EventBunch(
        String systemInn,
        String systemEventId,
        String proEventId,
        Status status
) {
}
