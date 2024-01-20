package ru.khokhlov.biletka.service;

import org.springframework.stereotype.Service;
import ru.khokhlov.biletka.dto.request.OrganizationAddEvent;
import ru.khokhlov.biletka.dto.request.OrganizationAddPlace;
import ru.khokhlov.biletka.dto.request.OrganizationRegistration;
import ru.khokhlov.biletka.dto.response.DeleteEventOrganization;
import ru.khokhlov.biletka.dto.response.DeleteSession;
import ru.khokhlov.biletka.dto.response.OrganizationResponse;
import ru.khokhlov.biletka.dto.response.SessionInfo;
import ru.khokhlov.biletka.dto.universal.MassivePublicEvents;
import ru.khokhlov.biletka.dto.universal.MassivePublicSessions;
import ru.khokhlov.biletka.dto.universal.PublicOrganization;
import ru.khokhlov.biletka.entity.Event;
import ru.khokhlov.biletka.entity.Organization;

@Service
public interface OrganizationService {
    Long getOrganizationIdByEmailAndPassword(String email, String password);

    /**
     * Функция создания организации
     *
     * @param organizationRegistration информация организации для регистрации
     * @return ответ об успешном(неуспешном) создании организации
     */
    OrganizationResponse createOrganization(OrganizationRegistration organizationRegistration);

    /**
     * Функция вывода организации по id
     * @param organizationId id организации
     * @return организацию
     */
    Organization getOrganizationById(Long organizationId);

    /**
     * Функция вывода организации по id
     * @param email id организации
     * @return организацию
     */
    Organization getOrganizationByEmail(String email);

    /**
     * Функция добавления площадок организации
     *
     * @param organizationAddPlace информация о площадке и id организации
     * @return ответ об успешном(неуспешном) добавлении площадки для организации
     */
    OrganizationResponse addPlaceInOrganization(OrganizationAddPlace organizationAddPlace);
    /**
     * Фукнция получения всех сессий мероприятий организации
     * @param organisationId id организации
     * @return массив сессий
     */
    MassivePublicSessions getAllSession(Long organisationId);
    /**
     * Фукнция получения всех мероприятий организации
     * @param organisationId id организации
     * @return массив мероприятий
     */
    MassivePublicEvents getAllEvent(Long organisationId);
    /**
     * Функция удаления сессий ивента у организации
     * @param organisationId id организации
     * @param eventId id ивента
     * @return возвращает успешное/неуспешное удаление сессий ивента
     */
    DeleteEventOrganization deleteEventOrganizationRelation(Long organisationId, Long eventId);
    /**
     * Функция удаления сиссии у организации
     * @param organisationId id организации
     * @param sessionId id сессии
     * @return возвращает успешное/неуспешное удаление сессии
     */
    DeleteSession deleteSession(Long organisationId, Long sessionId);

    /**
     * Функция остановки продаж билетов
     * @param organisationId id организации
     * @param sessionId id сессии
     * @param status статус продажи
     * @return информация о сессии
     */
    SessionInfo editSellSession(Long organisationId, Long sessionId, Boolean status);

    /**
     * Функция получения информации
     * @param organisationId id организации
     * @param sessionId id сессии
     * @return информация о сессии
     */
    SessionInfo getSession(Long organisationId, Long sessionId);

    /**
     * Функция подтверждения активной почты организации
     *
     * @param code код подтверждения
     * @return true - подтверждена организация
     */
    boolean isOrganizationActivate(String code);

    /**
     * Функция добавления мероприятия к организации
     *
     * @param organizationAddEvent информация об организации
     * @return ответ об организации
     */
    OrganizationResponse addEventInOrganization(OrganizationAddEvent organizationAddEvent);

    PublicOrganization infoOrganization(Integer userId);

    Event[] getUnevents(Long organizationId);
}
