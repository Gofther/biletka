package ru.khokhlov.biletka.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;

@Validated
public record TicketInfo(
        @NotNull(message = "Session id must not be null")
        @JsonProperty("session_id")
        Long sessionId,
        @NotNull(message = "Organization id must not be null")
        @JsonProperty("organization_id")
        Long organizationId,
        @NotNull(message = "Price must not be null")
        Integer price,
        @NotNull(message = "On sale must not be null")
        @JsonProperty("on_sale")
        Integer onSale
) {
}
