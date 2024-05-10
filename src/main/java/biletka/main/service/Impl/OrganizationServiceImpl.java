package biletka.main.service.Impl;

import biletka.main.Utils.JwtTokenUtils;
import biletka.main.dto.request.OrganizationRegistrationRequest;
import biletka.main.dto.response.OrganizationResponse;
import biletka.main.entity.Event;
import biletka.main.entity.Organization;
import biletka.main.entity.Place;
import biletka.main.entity.Users;
import biletka.main.enums.StatusUserEnum;
import biletka.main.repository.OrganizationRepository;
import biletka.main.service.OrganizationService;
import biletka.main.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
// не ту библиотеку инициализировал. Нужна была не ломбока

import java.sql.Timestamp;
import java.util.Date;
import java.util.Set;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Lazy))
@Slf4j
public class OrganizationServiceImpl implements OrganizationService {
    private final OrganizationRepository organizationRepository;
    private final JwtTokenUtils jwtTokenUtils;

    @Lazy
    private final UserService userService;


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

    /**
     * Метод получения организации по пользователю
     * @param user пользователь
     * @return организация
     */
    @Override
    public Organization getOrganizationByUser(Users user) {
        log.trace("OrganizationServiceImpl.getOrganizationByUser - user {}", user);
        return organizationRepository.findFirstByUser(user);
    }

    /**
     * Метод добавление площадки к организации
     * @param newPlace площадка
     */
    @Override
    public void addPlace(Organization organization, Place newPlace) {
        log.trace("OrganizationServiceImpl.addPlace - organization {}, newPlace {}", organization, newPlace);
        organization.addPlace(newPlace);

        organizationRepository.save(organization);
    }

    /**
     * Метод добавление мероприятия к организации
     * @param event мероприятие
     */
    @Override
    public void addEventAdmin(Organization organization, Event event) {
        log.trace("OrganizationServiceImpl.addEventAdmin - organization {}, event {}", organization, event);
        Set<Event> eventSet = organization.getAdminEventSet();
        eventSet.add(event);

        organization.addEvent(event);
        organization.setAdminEventSet(eventSet);

        organizationRepository.save(organization);
    }

    /**
     * Метод получения организации по токену
     * @param authorization - токен авторизации
     * @return организация
     */
    @Override
    public OrganizationResponse getOrganization(String authorization){
        log.trace("OrganizationServiceImpl.getAllOrganization - authorization {}", authorization);
        String userEmail = jwtTokenUtils.getUsernameFromToken(
                authorization.substring(7)
        );

        Users user = userService.getUserOrganizationByEmail(userEmail);

        if (user == null) {
            throw new EntityNotFoundException("A broken token!");
        }

        Organization organization = getOrganizationByUser(user);

        if (organization == null) {
            throw new EntityNotFoundException("A broken token!");
        }
        return new OrganizationResponse(
                organization.getId(),
                organization.getUser(),
                organization.getInn(),
                organization.getKbk(),
                organization.getKpp(),
                organization.getOgrn(),
                organization.getOktmo(),
                organization.getContactPhone(),
                organization.getEmail(),
                organization.getFullNameOrganization(),
                organization.getFullNameSignatory(),
                organization.getLegalAddress(),
                organization.getNamePayer(),
                organization.getPositionSignatory(),
                organization.getPostalAddress()
        );
    }
}
