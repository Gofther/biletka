package biletka.main.service.Impl;

import biletka.main.Utils.JwtTokenUtils;
import biletka.main.dto.request.OrganizationRegistrationRequest;
import biletka.main.dto.response.*;
import biletka.main.entity.*;
import biletka.main.enums.StatusUserEnum;
import biletka.main.repository.OrganizationRepository;
import biletka.main.service.HallService;
import biletka.main.service.OrganizationService;
import biletka.main.service.SessionService;
import biletka.main.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Lazy))
@Slf4j
public class OrganizationServiceImpl implements OrganizationService {
    private final OrganizationRepository organizationRepository;
    private final JwtTokenUtils jwtTokenUtils;

    @Lazy
    private final UserService userService;
    private final SessionService sessionService;
    private final HallService hallService;

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
                StatusUserEnum.ACTIVE
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

//    /**
//     * Метод добавление мероприятия к организации
//     * @param event мероприятие
//     */
//    @Override
//    public void addEventAdmin(Organization organization, Event event) {
//        log.trace("OrganizationServiceImpl.addEventAdmin - organization {}, event {}", organization, event);
//        Set<Event> eventSet = organization.getAdminEventSet();
//        eventSet.add(event);
//
//        organization.addEvent(event);
//        organization.setAdminEventSet(eventSet);
//
//        organizationRepository.save(organization);
//    }

    /**
     * Метод получения мероприятий организации
     * @param authorization токен авторизации
     * @return массив мероприятий и их количесто
     */
    @Override
    public EventsOrganization getEventsOrganization(String authorization) {
        log.trace("OrganizationServiceImpl.getEventsOrganization - authorization {}", authorization);

        Organization organization = tokenVerification(authorization);

        ArrayList<EventOrganization> eventsOrganization = new ArrayList<>();

        organization.getEventSet().forEach(event -> {
            eventsOrganization.add(
                    new EventOrganization(
                        event.getId(),
                        event.getEventBasicInformation().getName_rus(),
                        event.getEventBasicInformation().getSymbolicName(),
                        event.getRating(),
                        event.getDuration(),
                        event.getEventBasicInformation().getPushkin(),
                        event.getEventAdditionalInformation().getTagSet().toArray(Tag[]::new),
                        event.getEventBasicInformation().getGenres().toArray(Genre[]::new),
                        String.valueOf(event.getEventBasicInformation().getAgeRatingId().getLimitation()),
                        sessionService.getTotalByEventAndPlaces(event, organization.getPlaceSet())
                )
            );
        });

        return new EventsOrganization(
                eventsOrganization.size(),
                eventsOrganization.toArray(EventOrganization[]::new)
        );
    }

    /**
     * Метод получения площадок организации
     * @param authorization токен пользователя
     * @return массив площадок
     */
    @Override
    public PlacesOrganization getPlacesOrganization(String authorization) {
        log.trace("OrganizationServiceImpl.getPlacesOrganization - authorization {}", authorization);

        Organization organization = tokenVerification(authorization);

        ArrayList<PlaceOrganization> placeOrganizationArrayList = new ArrayList<>();

        organization.getPlaceSet().forEach(place -> {
            placeOrganizationArrayList.add(
                    new PlaceOrganization(
                            place.getId(),
                            place.getAddress(),
                            place.getCity().getCityName(),
                            place.getPlaceName(),
                            hallService.getTotalByPlace(place)
                    )
            );
        });

        return new PlacesOrganization(
                placeOrganizationArrayList.size(),
                placeOrganizationArrayList.toArray(PlaceOrganization[]::new)
        );
    }

    /**
     * Метод получения залов у организации
     * @param authorization токе авторизации
     * @return массив залов
     */
    @Override
    public MassivePlacesAndHalls getPlacesAndSession(String authorization) {
        log.trace("OrganizationServiceImpl.getPlacesAndSession - authorization {}", authorization);
        Organization organization = tokenVerification(authorization);

        ArrayList<PlaceHallOrganization> placeHallOrganizationArrayList = new ArrayList<>();

        organization.getPlaceSet().forEach(place -> {
            ArrayList<HallOrganization> hallOrganizationArrayList = new ArrayList<>();

            hallService.getAllHallByPlace(place).forEach(hall -> {
                hallOrganizationArrayList.add(
                        new HallOrganization(
                                hall.getId(),
                                hall.getHallNumber(),
                                hall.getHallName(),
                                hall.getNumberOfSeats(),
                                hall.getInfo(),
                                hall.getScheme() == null
                        )
                );
            });

            placeHallOrganizationArrayList.add(
                    new PlaceHallOrganization(
                            place.getId(),
                            place.getPlaceName(),
                            place.getAddress(),
                            place.getCity().getCityName(),
                            hallOrganizationArrayList.toArray(HallOrganization[]::new)
                    )
            );
        });

        return new MassivePlacesAndHalls(placeHallOrganizationArrayList.toArray(PlaceHallOrganization[]::new));
    }

    /**
     * Проверка токена авторизации и вывод организации
     * @param token токен авторизации
     * @return организация
     */
    public Organization tokenVerification(String token) {
        log.trace("OrganizationServiceImpl.tokenVerification - token {}", token);
        String userEmail = jwtTokenUtils.getUsernameFromToken(
                token.substring(7)
        );

        Users user = userService.getUserOrganizationByEmail(userEmail);

        if (user == null) {
            throw new EntityNotFoundException("A broken token!");
        }

        Organization organization = getOrganizationByUser(user);

        if (organization == null) {
            throw new EntityNotFoundException("A broken token!");
        }

        return organization;
    }
}
