package ru.khokhlov.biletka.dto.feign.work_with_ticket;

public record TicketRedemptionRequest(
        Long visitDate,
        String comment
) {
}
