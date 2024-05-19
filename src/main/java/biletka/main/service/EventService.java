package biletka.main.service;

import biletka.main.dto.request.EventCreateRequest;
import biletka.main.dto.response.MessageCreateResponse;
import biletka.main.dto.universal.MassivePublicEvent;
import biletka.main.dto.universal.PublicEventImage;
import biletka.main.dto.universal.PublicFullInfoEvent;
import biletka.main.entity.Event;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;

@Service
public interface EventService {
    /**
     * Метод создания и добавления мероприятия в бд
     * @param authorization токен авторизации
     * @param file изображение мероприятия
     * @param eventCreateRequest информация мероприятия
     * @return сообщение оуспешном создании мероприятия
     */
    MessageCreateResponse createEvent(String authorization, MultipartFile file, EventCreateRequest eventCreateRequest) throws IOException;

    /**
     * Метод получения мероприятия по id
     * @param id мероприятия
     * @return мероприятие
     */
    Event getEventById(Long id);

    /**
     * Метод получения изображения мероприятия
     * @param id мероприятия
     * @param symbolicName символичное название мероприятия
     * @return данные для изображения
     */
    PublicEventImage getImageEvent(String id, String symbolicName) throws IOException;

    /**
     * Метод получения кртакой информации о 10 мероприятиях по городу
     * @param cityName название города
     * @param authorization токен авторизации
     * @param offset отсчет мероприятий
     * @param date дата для выборки
     * @return массив краткой информации
     */
    MassivePublicEvent getEventLimit(String cityName, String authorization, Integer offset, Date date);

    /**
     * Метод получения анонсов 10 мероприятий по городу
     * @param cityName название города
     * @param authorization токен авторизации
     * @param offset отсчет мероприятий
     * @return массив краткой информации
     */
    MassivePublicEvent getAnnouncementLimit(String cityName, String authorization, Integer offset, Date date);

    /**
     * Метод получения полной информации меропрития и места проведения и его сеансов
     * @param authorization токе авторизации
     * @param cityName название города
     * @param eventName id и символьное название мероприятия
     * @param date дата для поиска
     * @return полная информация мероприятия
     */
    PublicFullInfoEvent getFullInfoEvent(String authorization, String cityName, String eventName, Date date);

    /**
     * Метод получения мероприятия по id и символьному названию
     * @param eventSymbolic id и символьное название
     * @return мероприятие
     */
    Event getEventByIdAndSymbolic(String eventSymbolic);

    /**
     * Метод изменения рейтинга мероприятия
     * @param event мероприятие
     * @param rating рейтинг пользователя
     */
    void putRatingEvent(Event event, Double rating);
}
