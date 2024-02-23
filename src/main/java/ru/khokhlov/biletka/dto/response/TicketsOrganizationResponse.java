package ru.khokhlov.biletka.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import ru.khokhlov.biletka.dto.response.ticketsOrganization_full.TicketsSessionOrganization;

public record TicketsOrganizationResponse(
        String address,

        @JsonProperty("place_name")
        String placeName,

        @JsonProperty("events")
        TicketsSessionOrganization[] ticketsSessionOrganizations
) {
}
