package biletka.main.service;

import biletka.main.dto.request.OrganizationRegistrationRequest;
import biletka.main.dto.response.EventsOrganization;
import biletka.main.dto.response.MessageCreateResponse;
import biletka.main.entity.Event;
import biletka.main.entity.Organization;
import biletka.main.entity.Place;
import biletka.main.entity.Users;
import org.springframework.stereotype.Service;

@Service
public interface OrganizationService {
    /**
     * Метод добавления организации в бд
     * @param organizationRegistrationRequest данные организации
     * @param user данные о новом пользователе
     */
    void postCreateOrganization(OrganizationRegistrationRequest organizationRegistrationRequest, Users user);

    /**
     * Метод получения организации по данным
     * @param organizationRegistrationRequest данные организации
     * @return организация
     */
    Organization getOrganizationByFullNameOrganization(OrganizationRegistrationRequest organizationRegistrationRequest);

    /**
     * Метод получения организации по пользователю
     * @param user пользователь
     * @return организация
     */
    Organization getOrganizationByUser(Users user);

    /**
     * Метод добавление площадки к организации
     * @param newPlace площадка
     */
    void addPlace(Organization organization, Place newPlace);

    /**
     * Метод добавление мероприятия к организации
     * @param event мероприятие
     */
    void addEventAdmin(Organization organization, Event event);

    /**
     * Метод получения мероприятий организации
     * @param authorization токен авторизации
     * @return массив мероприятий и их количесто
     */
    EventsOrganization getEventsOrganization(String authorization);
}
