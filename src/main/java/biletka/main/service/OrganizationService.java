package biletka.main.service;

import biletka.main.dto.request.OrganizationRegistrationRequest;
import biletka.main.dto.response.*;

import biletka.main.dto.response.TotalSession.TotalSession;
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
     * Метод добавления мероприятия к организации
     * @param eventId id мероприятия
     * @param authorization токен авторизации
     * @return успешное создание записи
     */
    MessageCreateResponse postEventOrganization(String authorization, Long eventId);
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

//    /**
//     * Метод добавление мероприятия к организации
//     * @param event мероприятие
//     */
//    void addEventAdmin(Organization organization, Event event);

    /**
     * Метод получения организации по токену
     * @param authorization - токен авторизации
     * @return организация
     */
    OrganizationResponse getOrganization(String authorization);

    /**
     * Метод получения сеансов по токену
     * @param authorization - токен авторизации
     * @return массив сеансов по площадкам и мероприятиям
     */
    TotalSession getSessionsByOrganization(String authorization);

    /**
     * Метод получения мероприятий организации
     * @param authorization токен пользователя
     * @return массив мероприятий и их количесто
     */
    EventsOrganization getEventsOrganization(String authorization);

    /**
     * Метод получения площадок организации
     * @param authorization токен пользователя
     * @return массив площадок
     */
    PlacesOrganization getPlacesOrganization(String authorization);

    /**
     * Метод получения залов у организации
     * @param authorization токе авторизации
     * @return массив залов
     */
    MassivePlacesAndHalls getPlacesAndSession(String authorization);

    /**
     * Метод получения статистики продаж за месяц
     * @param authorization токен авторизации
     * @return статистика продаж и возвратов
     */
    SalesResponse getMonthlySalesOrganization(String authorization);

    /**
     * Метод получения статистики продаж за месяц
     * @param authorization токен авторизации
     * @return статистика продаж и возвратов
     */
    YearlySalesResponse getYearlySalesOrganization(String authorization);
}
