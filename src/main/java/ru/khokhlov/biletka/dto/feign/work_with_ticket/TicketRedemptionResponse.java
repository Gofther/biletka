package ru.khokhlov.biletka.dto.feign.work_with_ticket;

import ru.khokhlov.biletka.enums.Status;

public record TicketRedemptionResponse(
        Long visitDate,
        Status status
) {
}
