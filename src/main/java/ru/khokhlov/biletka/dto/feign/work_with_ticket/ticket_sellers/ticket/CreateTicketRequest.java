package ru.khokhlov.biletka.dto.feign.work_with_ticket.ticket_sellers.ticket;

public record CreateTicketRequest(
        String barcode,
        String barcodeType,
        Visitor visitor,
        Buyer buyer,
        Session session,
        Payment payment,
        String comment
) {
}
