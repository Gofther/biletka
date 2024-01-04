package ru.khokhlov.biletka.dto.feign.work_with_ticket.ticket_sellers;

public record RefundTicketRequest(
        long refundDate,
        String refundReason,
        String refundRrn,
        String refundTicketPrice,
        String comment
) {
}
