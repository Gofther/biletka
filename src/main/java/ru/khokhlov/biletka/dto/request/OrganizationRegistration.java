package ru.khokhlov.biletka.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;
import org.springframework.validation.annotation.Validated;

@Validated
public record OrganizationRegistration(
        @NotBlank(message = "Name of organization must not be blank")
        @JsonProperty("full_name_organization")
        String fullNameOrganization,

        @NotBlank(message = "Legal address must not be blank")
        @JsonProperty("legal_address")
        String legalAddress,


        @NotNull(message = "Postal address must not be null")
        @JsonProperty("postal_address")
        Integer postalAddress,

        @NotBlank(message = "Phone number must not be blank")
        @Pattern(regexp = "^7\\d{10}", message = "Phone number must be a 10-digit number")
        @JsonProperty("contact_phone")
        String contactPhone,

        @NotBlank(message = "Email must not be blank")
        @Pattern(regexp = "\\b[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Z|a-z]{2,}\\b", message = "Invalid email format")
        String email,

        @NotBlank(message = "Full name signatory must not be blank")
        @JsonProperty("full_name_signatory")
        String fullNameSignatory,

        @NotBlank(message = "Position of signatory must not be blank")
        @JsonProperty("position_signatory")
        String positionSignatory,

        @NotBlank(message = "Document contract must not be blank")
        @JsonProperty("document_contract")
        String documentContract,

        @NotNull(message = "INN must not be null")
        @Positive(message = "INN must be a positive integer")
        Integer INN,

        @NotNull(message = "KPP must not be null")
        @Positive(message = "KPP must be a positive integer")
        Integer KPP,

        @NotNull(message = "OGRN must not be null")
        @Positive(message = "OGRN must be a positive integer")
        Integer OGRN,

        @NotNull(message = "OKTMO must not be null")
        @Positive(message = "OKTMO must be a positive integer")
        Integer OKTMO,

        @NotNull(message = "KBK must not be null")
        @Positive(message = "KBK must be a positive integer")
        Integer KBK,

        @NotBlank(message = "Name of payee must not be blank")
        @JsonProperty("name_payer")
        String namePayer,

        @NotBlank(message = "Password must not be blank")
        @Pattern(regexp = "^(?=.*[a-zа-я])(?=.*[A-ZА-Я])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$", message = "Invalid password format")
        String password
) {
}
