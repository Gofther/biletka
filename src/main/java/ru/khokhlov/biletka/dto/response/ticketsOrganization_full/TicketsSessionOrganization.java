package ru.khokhlov.biletka.dto.response.ticketsOrganization_full;

import com.fasterxml.jackson.annotation.JsonProperty;

public record TicketsSessionOrganization(
        @JsonProperty("event_name")
        String eventName,

        @JsonProperty("tickets")
        TicketOrganization[] ticketOrganizations
) {
}
