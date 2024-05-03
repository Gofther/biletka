package biletka.main.service.Impl;

import biletka.main.dto.request.OrganizationRegistrationRequest;
import biletka.main.dto.response.MessageCreateResponse;
import biletka.main.entity.Organization;
import biletka.main.entity.Users;
import biletka.main.enums.StatusUserEnum;
import biletka.main.exception.ErrorMessage;
import biletka.main.exception.InvalidDataException;
import biletka.main.repository.OrganizationRepository;
import biletka.main.service.OrganizationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrganizationServiceImpl implements OrganizationService {
    private final OrganizationRepository organizationRepository;

    /**
     * Метод добавления организации в бд
     * @param organizationRequest данные организации
     * @param user данные о новом пользователе
     */
    @Override
    public void postCreateOrganization(OrganizationRegistrationRequest organizationRequest, Users user) {
        log.trace("OrganizationServiceImpl.postCreateOrganization - organizationRequest {}, user {}", organizationRequest, user);
        Organization organization = new Organization(
                user,
                organizationRequest.inn(),
                organizationRequest.kbk(),
                organizationRequest.kpp(),
                organizationRequest.ogrn(),
                organizationRequest.oktmo(),
                organizationRequest.contactPhone(),
                organizationRequest.email(),
                organizationRequest.fullNameOrganization(),
                organizationRequest.fullNameSignatory(),
                organizationRequest.legalAddress(),
                organizationRequest.namePayer(),
                organizationRequest.positionSignatory(),
                Integer.valueOf(organizationRequest.postalAddress()),
                new Timestamp(new Date().getTime()),
                StatusUserEnum.ACTIVE,
                null
        );

        organizationRepository.saveAndFlush(organization);
    }

    /**
     * Метод получения организации по данным
     * @param organizationRequest данные организации
     * @return организация
     */
    @Override
    public Organization getOrganizationByFullNameOrganization(OrganizationRegistrationRequest organizationRequest) {
        log.trace("OrganizationServiceImpl.getOrganizationByFullNameOrganization - organizationRequest {}", organizationRequest);
        return organizationRepository.findFirstByFullInfo(
                organizationRequest.inn(),
                organizationRequest.kbk(),
                organizationRequest.kpp(),
                organizationRequest.ogrn(),
                organizationRequest.oktmo(),
                organizationRequest.email(),
                organizationRequest.fullNameOrganization()
        );
    }
}
