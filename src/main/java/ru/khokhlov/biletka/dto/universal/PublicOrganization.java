package ru.khokhlov.biletka.dto.universal;

public record PublicOrganization(
        Long id,
        String fullNameOrganization,
        String legalAddress,
        String postalAddress,
        String contactPhone,
        String email,
        String fullNameSignatory,
        String positionSignatory,
        String INN,
        String KPP,
        String OGRN,
        String OKTMO,
        String KBK
) {
}
