package ru.khokhlov.biletka.dto.feign.work_with_ticket.ticket_sellers.ticket;

public record Payment(
        String id,
        String rrn,
        long date,
        String ticketPrice,
        String amount
) {
}
