package ru.khokhlov.biletka.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;

@Validated
public record OrganizationAddPlace(
        String[] places,
        @NotNull(message = "Organization id must not be null")
        @JsonProperty("organization_id")
        Long organizationId,
        @NotBlank(message = "City must not be blank")
        String city
) {
}
