package biletka.main.dto.response;

import biletka.main.entity.Event;
import biletka.main.entity.Users;
import biletka.main.enums.StatusUserEnum;
import jakarta.persistence.*;
import org.hibernate.annotations.BatchSize;

import java.sql.Timestamp;
import java.util.Set;

public record OrganizationResponse(
        Long id,
        Users user,
        String inn,
        String kbk,
        String kpp,
        String ogrn,
        String oktmo,
        String contactPhone,
        String email,
        String fullNameOrganization,
        String fullNameSignatory,
        String legalAddress,
        String namePayer,
        String positionSignatory,
        Integer postalAddress
) {
}
