package ru.khokhlov.biletka.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import ru.khokhlov.biletka.entity.MovieViewType;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record SessionResponseByTicket(
        String name,
        String address,
        TicketsInfoResponse[] sessions
) {
}
