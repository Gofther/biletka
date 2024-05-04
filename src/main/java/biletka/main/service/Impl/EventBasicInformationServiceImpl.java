package biletka.main.service.Impl;
import biletka.main.dto.request.EnentItem.EventBasicInformationRequest;
import biletka.main.entity.AgeRating;
import biletka.main.entity.Genre;
import biletka.main.entity.TypeEvent;
import biletka.main.entity.event_item.EventBasicInformation;
import biletka.main.exception.ErrorMessage;
import biletka.main.exception.InvalidDataException;
import biletka.main.repository.ActorRepository;
import biletka.main.repository.EventBasicInformationRepository;
import biletka.main.service.AgeRatingService;
import biletka.main.service.EventBasicInformationService;
import biletka.main.service.GenreService;
import biletka.main.service.TypeEventService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Service
@RequiredArgsConstructor
@Slf4j
public class EventBasicInformationServiceImpl implements EventBasicInformationService {
    private final EventBasicInformationRepository eventBasicInformationRepository;
    private final AgeRatingService ageRatingService;
    private final TypeEventService typeEventService;
    private final GenreService genreService;

    /**
     * Метод Создания новогой информации о событии и сохранения в бд
     * @param eventBasicInformationRequest - информация о событии
     * @return Инициализированная информация о событии
     */
    @Override
    public EventBasicInformation postNewEventBasicInformation(EventBasicInformationRequest eventBasicInformationRequest){
        log.trace("EventBasicInformationService.postNewEventBasicInformation - EventBasicInformationRequest{}", eventBasicInformationRequest);

        EventBasicInformation eventBasicInformation = eventBasicInformationRepository.findFirstByName(eventBasicInformationRequest.name());

        if(eventBasicInformation!= null){
            List<ErrorMessage> errorMessages = new ArrayList<>();
            errorMessages.add(new ErrorMessage("Post error", "This event basic information already exists!"));
            throw new InvalidDataException(errorMessages);
        }

        String[] genre = eventBasicInformationRequest.genre();
        Set<Genre> genres = new HashSet<>();
        for (String s : genre) {
            genres.add(genreService.getGenreOfName(s));
        }

        EventBasicInformation newEventBasicInformation = new EventBasicInformation(
                eventBasicInformationRequest.name(),
                null,
                eventBasicInformationRequest.nameRus(),
                eventBasicInformationRequest.organizer(),
                ageRatingService.getAgeRatingOfLimitation(eventBasicInformationRequest.ageRating()),
                typeEventService.getTypeEventOfName(eventBasicInformationRequest.eventType()),
                eventBasicInformationRequest.pushkin(),
                eventBasicInformationRequest.eventIDCulture(),
                eventBasicInformationRequest.showInPoster(),
                eventBasicInformationRequest.img(),
                genres
        );

        eventBasicInformationRepository.saveAndFlush(newEventBasicInformation);
        return newEventBasicInformation;
    }
}
