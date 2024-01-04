package ru.khokhlov.biletka.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ru.khokhlov.biletka.entity.Event;
import ru.khokhlov.biletka.entity.Place;

import java.util.List;

public record OrganizationResponse(
        Long id,
        String fullNameOrganization,
        String legalAddress,
        Integer postalAddress,
        String contactPhone,
        String email,
        String activationCode,
        String fullNameSignatory,
        String positionSignatory,
        String documentContract,
        Integer INN,
        Integer KPP,
        Integer OGRN,
        Integer OKTMO,
        Integer KBK,
        String namePayer,
        String password,
        List<Place> placeList,
        @JsonIgnoreProperties({"hibernateLazyInitializer", "eventList"})
        List<Event> eventList
) {
}
