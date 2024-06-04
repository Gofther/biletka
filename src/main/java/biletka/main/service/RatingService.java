package biletka.main.service;

import biletka.main.entity.Client;
import biletka.main.entity.Event;
import biletka.main.entity.Rating;
import org.springframework.stereotype.Service;

@Service
public interface RatingService {
    /**
     * Метод получения рейтинга мероприятия, поставленным пользователем
     * @param client пользователь
     * @param event мероприятие
     * @return рейтинг
     */
    Rating getRatingInClientAndEvent(Client client, Event event);

    /**
     * Метод созданий рейтинга
     * @param client пользователь
     * @param event мероприятие
     * @param rating рейтинг, оставленный пользователем
     */
    void createRating(Client client, Event event, Double rating);

    /**
     * Метод подсчета количества поставленного рейтинга пользователями
     * @param event мероприятие
     * @return количество поставленных рейтингов
     */
    Double getTotalRatingByEvent(Event event);
}
