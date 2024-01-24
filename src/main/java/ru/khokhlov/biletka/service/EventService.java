package ru.khokhlov.biletka.service;

import org.springframework.stereotype.Service;
import ru.khokhlov.biletka.dto.request.EventInfo;
import ru.khokhlov.biletka.dto.response.*;
import ru.khokhlov.biletka.dto.universal.MassivePublicEvents;
import ru.khokhlov.biletka.entity.Event;
import ru.khokhlov.biletka.entity.EventImage;

import java.time.LocalDate;
import java.util.List;

@Service
public interface EventService {

    /**
     * Функция получения и создания события
     * @param eventInfo информация о событии
     * @return символьное имя события
     */
    EventResponse createEvent(EventInfo eventInfo);


    /**
     * Получение дат данного события в городе
     * @param eventSymbolicName символьное имя
     * @param city город события
     * @param eventId id ивента
     * @return массив дат проведения данного события
     */
    DatesOfEvents getDateOfEvent(String eventSymbolicName, String city, Long eventId);

    /**
     * Получение деталей информации о событии
     * @param eventSymbolicName символьное имя
     * @param city город события
     * @param eventId id ивента
     * @return полная информация об этом событии
     */
    EventDetail getEventDetail(String eventSymbolicName, String city, Long eventId);

    /**
     * Функция получения массива ближайших событий на неделю
     * @param city город
     * @return массив событий
     */
    MassiveOfEvents getNearestEvents(String city);

    /**
     * Получение массива мероприятия
     *
     * @param city город мероприятия
     * @param placeName название площадки
     * @param date дата мероприятия
     * @return массив мероприятий
     */
    MassivePublicEvents getAllEventByDate(String city, String placeName, LocalDate date);

    /**
     * Функция получения ивента
     *
     * @param eventSymbolicName символьное имя
     * @param eventType тип мероприятия
     * @return возвращает ивент
     */
    Event getEventBySymbolicNameAndType(String eventSymbolicName, String eventType);

    /**
     * Функция получения ивента
     *
     * @param eventId id ивента
     * @return возвращает ивент
     */
    Event getEventById(Long eventId);

    /**
     * Функция получения всех ивентов
     * @return массив ивентов
     */
    MassiveOfEvents getAllEvent();

    /**
     * Функция получения массива площадок ивента и небольшой информации
     *
     * @param city название города ивента
     * @param id   id ивента
     * @return массива площадок ивента и небольшой информации
     */
    PlacesAndInfoSession[] getPlacesAndInfo(String city, Long id);

    /**
     * Удаление события по id
     *
     * @param id   id ивента
     * @return информация о событии
     */
    EventResponse removeEventById(Long id);

    List<Event> getAllFullInfo();

    Event[] getEventByType(String name, Integer offset);

    void addImageEvent(Long eventId, EventImage eventImage);
}
