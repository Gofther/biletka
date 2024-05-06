package biletka.main.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record OrganizationRegistrationRequest(
        @Pattern(regexp = "\\b[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}", message = "Invalid email format")
        @NotBlank(message = "Email is mandatory!")
        String email,

        @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,25}$", message = "Invalid password email")
        @NotBlank(message = "Password is mandatory!")
        String password,

        @Pattern(regexp = "^7\\d{10}$", message = "Invalid Russian phone number format 79876543210")
        @JsonProperty("contact_phone")
        @NotBlank(message = "Contact phone is mandatory!")
        String contactPhone,

        @JsonProperty("full_name_organization")
        @NotBlank(message = "Full name organization is mandatory!")
        String fullNameOrganization,

        @Pattern(regexp = "^[A-Za-zА-Яа-я]+\\s+[A-Za-zА-Яа-я]+\\s+[A-Za-zА-Яа-я]+$", message = "Invalid full name format")
        @JsonProperty("full_name_signatory")
        @NotBlank(message = "Full name signatory is mandatory!")
        String fullNameSignatory,

        @Size(min = 10, max = 10)
        @JsonProperty("inn")
        @NotBlank(message = "Inn is mandatory!")
        String inn,

        @Size(min = 20, max = 20)
        @JsonProperty("kbk")
        @NotBlank(message = "Kbk is mandatory!")
        String kbk,

        @Size(min = 9, max = 9)
        @JsonProperty("kpp")
        @NotBlank(message = "Kpp is mandatory!")
        String kpp,

        @Size(min = 13, max = 13)
        @JsonProperty("ogrn")
        @NotBlank(message = "Ogrn is mandatory!")
        String ogrn,

        @Size(min = 11, max = 11)
        @JsonProperty("oktmo")
        @NotBlank(message = "Oktmo is mandatory!")
        String oktmo,

        @JsonProperty("legal_address")
        @NotBlank(message = "Legal address is mandatory!")
        String legalAddress,

        @Pattern(regexp = "^[A-Za-zА-Яа-я]+\\s+[A-Za-zА-Яа-я]+\\s+[A-Za-zА-Яа-я]+$", message = "Invalid full name format")
        @JsonProperty("name_payer")
        @NotBlank(message = "Name payer is mandatory!")
        String namePayer,

        @JsonProperty("position_signatory")
        @NotBlank(message = "Position signatory is mandatory!")
        String positionSignatory,

        @Size(min = 6, max = 6)
        @JsonProperty("postal_address")
        @NotBlank(message = "Postal address is mandatory!")
        String postalAddress
) {
}
