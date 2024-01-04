package ru.khokhlov.biletka.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;

@Validated
public record OrganizationAddEvent(
        @NotNull(message = "Event id must not be null")
        @JsonProperty("event_id")
        Long eventId,
        @NotNull(message = "Organization id must not be null")
        @JsonProperty("organization_id")
        Long organizationId
) {
}
