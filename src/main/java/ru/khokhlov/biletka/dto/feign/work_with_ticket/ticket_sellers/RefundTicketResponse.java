package ru.khokhlov.biletka.dto.feign.work_with_ticket.ticket_sellers;

import ru.khokhlov.biletka.enums.Status;

public record RefundTicketResponse(
        long refundDate,
        String refundReason,
        String refundRrn,
        String refundTicketPrice,
        Status status
) {
}
