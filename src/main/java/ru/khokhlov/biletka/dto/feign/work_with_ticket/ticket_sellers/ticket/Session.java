package ru.khokhlov.biletka.dto.feign.work_with_ticket.ticket_sellers.ticket;

public record Session(
        String eventId,
        String organizationId,
        long date,
        String place,
        String params
) {
}
