package biletka.main.service;

import biletka.main.dto.request.SessionCreateRequest;
import biletka.main.dto.response.MessageCreateResponse;
import biletka.main.entity.*;
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
     * Метод получения уникальных мероприятий по возрасту
     * @param city город
     * @param age возраст
     * @param offset отступ
     * @param date дата для выборки
     * @return массив мероприятий
     */
    Set<Event> getMassiveEventByCityAndAgeLimit(City city, int age, Integer offset, Date date);

    Set<Event> getMassiveEventByCityAndType(City city, String type, Integer offset, Date date);

    Set<Event> getMassiveEventByCityAndGenre(City city, Genre genre, Integer offset, Date date);

    int getSumSession(Place place);
}
