package ru.khokhlov.biletka.dto.feign.work_with_ticket.ticket_sellers.ticket;

public record Visitor(
        String fullName,
        String firstName,
        String middleName,
        String lastName
) {
}
