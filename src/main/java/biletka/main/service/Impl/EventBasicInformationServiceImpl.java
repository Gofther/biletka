package biletka.main.service.Impl;

import biletka.main.Utils.ConvertUtils;
import biletka.main.dto.request.event_item.EventBasicRequest;
import biletka.main.entity.AgeRating;
import biletka.main.entity.Genre;
import biletka.main.entity.TypeEvent;
import biletka.main.entity.event_item.EventBasicInformation;

import biletka.main.repository.EventBasicInformationRepository;
import biletka.main.service.AgeRatingService;
import biletka.main.service.EventBasicInformationService;
import biletka.main.service.GenreService;
import biletka.main.service.TypeEventService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;


@Service
@RequiredArgsConstructor
@Slf4j
public class EventBasicInformationServiceImpl implements EventBasicInformationService {
    private final EventBasicInformationRepository eventBasicInformationRepository;
    private final ConvertUtils convertUtils;

    private final TypeEventService typeEventService;
    private final AgeRatingService ageRatingService;
    private final GenreService genreService;

    /**
     * Метод создания и сохранения основной информации о мероприятии в бд
     * @param eventBasicRequest основная информация для создания
     * @return основная информация о мероприятии
     */
    @Override
    public EventBasicInformation createEventBasic(EventBasicRequest eventBasicRequest, String fullNameFile) {
        log.trace("EventBasicInformationServiceImpl.createEventBasic - eventBasicRequest {}, fullNameFile {}", eventBasicRequest, fullNameFile);
        /** Проверка на существование типа мероприятия */
        TypeEvent type = typeEventService.getTypeEventOfName(eventBasicRequest.typeEvent());

        if (type == null) {
            type = typeEventService.createTypeEvent(eventBasicRequest.typeEvent());
        }

        /** Получение возрастного ограничения */
        AgeRating ageRating = ageRatingService.getAgeRatingOfLimitation(eventBasicRequest.ageRating());

        /** Проверка на существование жанров */
        Set<Genre> genreSet = new HashSet<>();

        for (String genreRequest: eventBasicRequest.genres()) {
            Genre genre = genreService.getGenreOfName(genreRequest);

            if (genre == null) {
                genre = genreService.createGenre(genreRequest);
            }

            genreSet.add(genre);
        }

        /** Сохранение в бд */
        EventBasicInformation eventBasicInformation = new EventBasicInformation(
                eventBasicRequest.name(),
                convertUtils.convertToSymbolicString(eventBasicRequest.name()),
                eventBasicRequest.nameRus(),
                eventBasicRequest.organizaer(),
                ageRating,
                type,
                eventBasicRequest.pushkin(),
                eventBasicRequest.eventIdCulture(),
                eventBasicRequest.showInPoster(),
                fullNameFile,
                genreSet
        );

        eventBasicInformationRepository.saveAndFlush(eventBasicInformation);

        return eventBasicInformation;
    }
}
