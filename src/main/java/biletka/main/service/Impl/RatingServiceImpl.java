package biletka.main.service.Impl;

import biletka.main.entity.Client;
import biletka.main.entity.Event;
import biletka.main.entity.Rating;
import biletka.main.exception.ErrorMessage;
import biletka.main.exception.InvalidDataException;
import biletka.main.repository.RatingRepository;
import biletka.main.service.RatingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class RatingServiceImpl implements RatingService {
    private final RatingRepository ratingRepository;

    /**
     * Метод получения рейтинга мероприятия, поставленным пользователем
     * @param client пользователь
     * @param event мероприятие
     * @return рейтинг
     */
    @Override
    public Rating getRatingInClientAndEvent(Client client, Event event) {
        log.trace("RatingServiceImpl.getRatingInClientAndEvent - client {}, event {}", client, event);
        return ratingRepository.findFirstByClientAndEvent(client, event);
    }

    @Override
    public void createRating(Client client, Event event, Double rating) {
        log.trace("RatingServiceImpl.createRating - client {}, event {}, rating {}", client, event, rating);
        Rating ratingEntity = ratingRepository.findFirstByClientAndEvent(client, event);

        if (ratingEntity != null) {
            List<ErrorMessage> errorMessages = new ArrayList<>();
            errorMessages.add(new ErrorMessage("Rating error", "The user has already rated the event!"));
            throw new InvalidDataException(errorMessages);
        }

        ratingEntity = new Rating(
                rating,
                client,
                event
        );
        ratingRepository.saveAndFlush(ratingEntity);
    }

    @Override
    public Double getTotalRatingByEvent(Event event) {
        log.trace("RatingServiceImpl.getTotalRatingByEvent - event {}", event);
        return ratingRepository.findTotalRatingByEvent(event);
    }
}
