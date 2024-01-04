package ru.khokhlov.biletka.dto.feign.work_with_ticket.ticket_controller;

import ru.khokhlov.biletka.dto.feign.work_with_ticket.ticket_sellers.ticket.Session;
import ru.khokhlov.biletka.enums.Status;

public record Bardcode(
        Status status,
        Session session
) {
}
