package ru.khokhlov.biletka.dto.feign.work_with_ticket.ticket_sellers.ticket;

public record FullTicketInfo(
        String id,
        String barcode,
        String status,
        Visitor visitor,
        Buyer buyer,
        Session session,
        Payment payment,
        long visitDate,
        long refundDate,
        String refundReason,
        String refundRrn,
        String refundTicketPrice,
        String comment
) {
}
