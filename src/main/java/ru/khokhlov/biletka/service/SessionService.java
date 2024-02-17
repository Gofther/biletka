package ru.khokhlov.biletka.service;

import org.springframework.stereotype.Service;
import ru.khokhlov.biletka.dto.request.SessionInfo;
import ru.khokhlov.biletka.dto.response.DeleteSession;
import ru.khokhlov.biletka.dto.response.SessionResponse;
import ru.khokhlov.biletka.dto.response.SessionResponseByTicket;
import ru.khokhlov.biletka.dto.response.SessionWidgetResponse;
import ru.khokhlov.biletka.dto.universal.PublicEvent;
import ru.khokhlov.biletka.dto.universal.PublicSession;
import ru.khokhlov.biletka.entity.Event;
import ru.khokhlov.biletka.entity.Place;
import ru.khokhlov.biletka.entity.Session;

import java.time.LocalDate;
import java.util.List;

@Service
public interface SessionService {
    /**
     * Функция получения массива дат сессий по id мероприятия id города
     * @param cityId id города
     * @param eventId id мероприятия
     * @return массив сессий мероприятий
     */
    String[] getMassiveDistinctDatesByEventAndCity(Long eventId, Long cityId);

    /**
     * Функция получения массива сессий мероприятий по городу
     * @param cityId id города
     * @return массив сессий мероприятий
     */
    PublicEvent[] getMassiveDistinctEventByCityAndDates(Long cityId);

    /**
     * Функция получения массива сессий мероприятий по городу и дате
     * @param date Дата
     * @param placeId Место
     * @return массив сессий мероприятий
     */
    PublicEvent[] getMassiveDistinctEventByPlaceAndDates(Long placeId, LocalDate date);

    /**
     * Функция создания сессии ивента
     *
     * @param city название города
     * @param sessionInfo информация о сессии ивента
     * @return ответ об успешном(неуспешном) создании сессии ивента
     */
    SessionResponse createSession(String city, SessionInfo sessionInfo);

    /**
     * Функция получения массива сессий мероприятий по городу и фильтру
     * @param city название города
     * @param date Дата
     * @param place Место
     * @return массив сессий мероприятий
     */
    PublicSession[] getMassiveBySessionFilter(LocalDate date, String place, String city);

    /**
     * Функция получения массива сессий мероприятий по площадкам
     * @param placeId id площадки
     * @return массив сессий
     */
    List<Session> getMassiveByPlace(Long placeId);

    SessionResponseByTicket[] getMassiveByEvent(Long eventId, String city, LocalDate date);
    /**
     * Функция удаления всех сессий ивента
     * @param event информация ивента
     * @param places массив площадок
     * @return возвращает успешное/неуспешное удаление сессии
     */
    String deleteAllSessionByEventAndPlaces(Event event, Place[] places);

    /**
     * Функция удаления сессии по id
     * @param sessionId id сессии
     * @return возвращает успешное/неуспешное удаление сессии
     */
    DeleteSession deleteSessionById(Long sessionId);

    /**
     * Функция получения сессии по id
     * @param sessionId id сессии
     * @return возвращает успешное/неуспешное получение сессии
     */
    Session getSessionById(Long sessionId);

    /**
     * Функция поиска сессий по информации
     * @param city название города
     * @param organizationId информация виджета для поиска
     * @return массив сессий
     */
    SessionWidgetResponse[] getMassiveByWidget(String city, Long organizationId);

    /**
     * Функция получения массива сессий
     * @param city  название города
     * @param event ивент
     * @return массив сессий
     */
    List<Session> getMassiveSessionByEventAndCity(Long city, Long event);
}
