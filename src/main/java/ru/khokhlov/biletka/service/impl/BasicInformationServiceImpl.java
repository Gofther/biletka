package ru.khokhlov.biletka.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.khokhlov.biletka.dto.request.EventSymbolicName;
import ru.khokhlov.biletka.dto.request.event_full_ei.BasicInformationRequest;
import ru.khokhlov.biletka.entity.EventBasicInformation;
import ru.khokhlov.biletka.entity.EventImage;
import ru.khokhlov.biletka.exception.ErrorMessage;
import ru.khokhlov.biletka.exception.InvalidDataException;
import ru.khokhlov.biletka.repository.BasicInformationRepository;
import ru.khokhlov.biletka.service.AgeRatingService;
import ru.khokhlov.biletka.service.BasicInformationService;
import ru.khokhlov.biletka.service.EventTypeService;
import ru.khokhlov.biletka.service.GenreService;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class BasicInformationServiceImpl implements BasicInformationService {
    private final BasicInformationRepository basicInformationRepository;
    private final AgeRatingService ageRatingService;
    private final EventTypeService eventTypeService;
    private final GenreService genreService;

    @Override
    public EventBasicInformation createBasicInformation(BasicInformationRequest basicInformationRequest) throws InvalidDataException {
        log.trace("BasicInformationServiceImpl.createBasicInformation - basicInformationRequest {}", basicInformationRequest);

        Integer ageRating = basicInformationRequest.ageRating();
        String eventType = basicInformationRequest.eventType();
        String[] genres = basicInformationRequest.genre();


        if (basicInformationRepository.findFirstByName(basicInformationRequest.name()) != null) {
            List<ErrorMessage> errorMessages = new ArrayList<>();
            errorMessages.add(new ErrorMessage("BasicInformationRequest", "Basic information is already exist!"));
            throw new InvalidDataException(errorMessages);
        }
        if (ageRatingService.getIdByLimitation(ageRating) == -1)
            ageRatingService.createAgeRating(ageRating);

        if (eventTypeService.getIdByType(eventType) == -1)
            eventTypeService.createEventType(eventType);

        for (String g : genres) {
            if (genreService.getIdByGenre(g) == -1)
                genreService.createGenre(g);
        }

        EventSymbolicName symbolicName = new EventSymbolicName(rebuildName(basicInformationRequest.name()));

        EventBasicInformation eventBasicInformation = new EventBasicInformation(
                basicInformationRequest.name(),
                basicInformationRequest.nameRus(),
                symbolicName.symbolicName(),
                basicInformationRequest.organizer(),
                basicInformationRequest.img_url(),
                basicInformationRequest.pushkin(),
                basicInformationRequest.eventIDCulture(),
                basicInformationRequest.showInPoster()
        );

        eventBasicInformation.setGenres(genreService.getAllGenreByStringMassive(basicInformationRequest.genre()));
        eventBasicInformation.setEventType(eventTypeService.getEventType(basicInformationRequest.eventType()));
        eventBasicInformation.setAgeRatingId(ageRatingService.getAgeRating(basicInformationRequest.ageRating()));

        basicInformationRepository.saveAndFlush(eventBasicInformation);

        return eventBasicInformation;
    }

    @Override
    public void deleteBasicInformation(EventBasicInformation eventBasicInformation) {
        basicInformationRepository.delete(eventBasicInformation);
    }

    @Override
    public void addImage(EventBasicInformation eventBasicInformation, EventImage eventImage) {
        eventBasicInformation.setEventImage(eventImage);
        basicInformationRepository.saveAndFlush(eventBasicInformation);
    }


    String rebuildName(String name) {
        log.debug("BasicInformationServiceImpl.rebuildName - String {}", name);
        String slug = name.toLowerCase(); // Преобразование к нижнему регистру
        slug = slug.replaceAll("[^a-zA-Z0-9\\s-]", ""); // Удаление символов, кроме букв, цифр, пробелов и дефисов
        slug = slug.replaceAll("\\s+", "-"); // Замена пробелов на дефисы
        slug = slug.replaceAll("-+", "-"); // Удаление повторяющихся дефисов

        return slug;
    }
}
