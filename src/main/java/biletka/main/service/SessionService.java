package biletka.main.service;

import biletka.main.dto.request.SessionCreateRequest;
import biletka.main.dto.response.HallSchemeResponse;
import biletka.main.dto.response.MessageCreateResponse;
import biletka.main.entity.City;
import biletka.main.entity.Event;
import biletka.main.entity.Place;
import biletka.main.entity.Session;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.Set;

@Service
public interface SessionService {
    /**
     * Метод создания и сохранения сеанса мероприятия в бд
     * @param authorization токен авторизации
     * @param sessionCreateRequest информация о сеансе
     * @return о успешном создании сеанса
     */
    MessageCreateResponse sessionCreate(String authorization, SessionCreateRequest sessionCreateRequest);

    /**
     * Метод получения уникальных мероприятий по сеансам
     * @param city город
     * @param offset отступ
     * @param date дата для выборки
     * @return массив мероприятий
     */
    Set<Event> getMassiveEventByCityLimit(City city, Integer offset, Date date);

    /**
     * Метод получения уникальных мероприятий по сеансам и дате создания мероприятия
     * @param city город
     * @param offset отступ
     * @param date дата для выборки
     * @return массив мероприятий
     */
    Set<Event> getMassiveAnnouncementByCityLimit(City city, Integer offset, Date date);

    /**
     * Метод получения сеансов мероприятия по городу и дате
     * @param event мероприятие
     * @param city город
     * @param date дата для поиска
     * @return массив сеансов
     */
    ArrayList<Session> getSessionsByEvent(Event event, City city, Date date);

    /**
     * Получение количества сеансов мероприятия организации
     * @param event мероприятие
     * @param placeSet площадки
     * @return количество сеансов мероприятия
     */
    Integer getTotalByEventAndPlaces(Event event, Set<Place> placeSet);

    /**
     * Метод получения сеансов мероприятия id
     * @param sessionId id сеанса
     * @return массив сеансов
     */
    Session getSessionById(Long sessionId);

    /**
     * Получение схемы зала по сессии
     * @param authorization токен авторизации пользователя
     * @param sessionId id сессии
     * @return схема зала
     */
    HallSchemeResponse getSessionHallScheme(String authorization, Long sessionId);
}
