package ru.khokhlov.biletka.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import ru.khokhlov.biletka.dto.request.EventInfo;
import ru.khokhlov.biletka.dto.response.*;
import ru.khokhlov.biletka.dto.universal.MassivePublicEvents;
import ru.khokhlov.biletka.dto.universal.PublicEvent;
import ru.khokhlov.biletka.entity.EventAdditionalInformation;
import ru.khokhlov.biletka.entity.EventBasicInformation;
import ru.khokhlov.biletka.entity.EventDuration;
import ru.khokhlov.biletka.entity.EventWebWidget;
import ru.khokhlov.biletka.entity.*;
import ru.khokhlov.biletka.repository.EventRepository;
import ru.khokhlov.biletka.service.*;
import ru.khokhlov.biletka.utils.NameRebuilder;

import java.time.LocalDate;
import java.util.*;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Lazy))
@Slf4j
public class EventServiceImpl implements EventService {
    private final EventRepository eventRepository;
    private final CityService cityService;
    private final PlaceService placeService;

    @Lazy
    private final SessionService sessionService;
    private final EventTypeService eventTypeService;
    private final AdditionalInformationService additionalInformationService;
    private final BasicInformationService basicInformationService;
    private final DurationService durationService;
    private final WebWidgetService webWidgetService;

    @Override
    public EventResponse createEvent(EventInfo eventInfo) throws EntityNotFoundException {
        log.trace("EventServiceImpl.createEvent - eventInfo {}", eventInfo);

        Event event = new Event();

        EventBasicInformation eventBasicInformation = basicInformationService.createBasicInformation(eventInfo.basicInformation());
        EventDuration eventDuration = durationService.createDuration(eventInfo.duration());
        EventAdditionalInformation eventAdditionalInformation = additionalInformationService.createAdditionalInformation(eventInfo.additionalInformationRequest());
        EventWebWidget eventWebWidget = webWidgetService.createWebWidget(eventInfo.webWidget());

        event.setEventBasicInformation(eventBasicInformation);
        event.setEventDuration(eventDuration);
        event.setEventAdditionalInformation(eventAdditionalInformation);
        event.setEventWebWidget(eventWebWidget);

        eventRepository.saveAndFlush(event);

        return new EventResponse(
                event.getId(),
                event.getEventBasicInformation().getSymbolicName()
        );
    }

    @Override
    public DatesOfEvents getDateOfEvent(String eventSymbolicName, String city, Long eventId) throws EntityNotFoundException {
        log.trace("EventServiceImpl.getDateOfEvent - city {}, eventId {}", city, eventId);

        Event event = eventRepository.getReferenceById(eventId);

        if (!Objects.equals(event.getEventBasicInformation().getSymbolicName(), eventSymbolicName)) {
            throw new EntityNotFoundException("Event with id " + eventId + " and symbolic name " + eventSymbolicName + " not found");
        }

        String[] date = sessionService.getMassiveDistinctDatesByEventAndCity(
                event.getId(),
                cityService.getCity(city).getCityId());

        return new DatesOfEvents(date);
    }

    @Override
    public EventDetail getEventDetail(String eventSymbolicName, String city, Long eventId) throws EntityNotFoundException {
        log.trace("EventServiceImpl.getEventDetail - city {}, eventId {}", city, eventId);

        Event event = eventRepository.getReferenceById(eventId);

        if (!Objects.equals(event.getEventBasicInformation().getSymbolicName(), eventSymbolicName)) {
            throw new EntityNotFoundException("Event with id " + eventId + " and symbolic name " + eventSymbolicName + " not found");
        }

        List<String> tagList = new ArrayList<>();
        List<String> actorsList = new ArrayList<>();
        List<String> genres = new ArrayList<>();

        for (Tag t : event.getEventAdditionalInformation().getTags()) {
            tagList.add(t.getName());
        }
        for (Actor a : event.getEventAdditionalInformation().getActors()) {
            tagList.add(a.getName());
        }
        for (Genre g : event.getEventBasicInformation().getGenres()) {
            genres.add(g.getName());
        }

        ru.khokhlov.biletka.dto.response.EventBasicInformation eventBasicInformation = new ru.khokhlov.biletka.dto.response.EventBasicInformation(
                event.getEventBasicInformation().getName(),
                event.getEventBasicInformation().getNameRus(),
                event.getEventBasicInformation().getSymbolicName(),
                event.getEventBasicInformation().getEventType().getType(),
                genres,
                event.getEventBasicInformation().getAgeRatingId().getLimitation(),
                event.getEventBasicInformation().getOrganizer(),
                event.getEventBasicInformation().getImg(),
                event.getEventBasicInformation().getPushkin()
        );
        ru.khokhlov.biletka.dto.response.EventDuration eventDuration = new ru.khokhlov.biletka.dto.response.EventDuration(
                event.getEventDuration().getHours(),
                event.getEventDuration().getMinutes()
        );
        ru.khokhlov.biletka.dto.response.EventAdditionalInformation eventAdditionalInformation = new ru.khokhlov.biletka.dto.response.EventAdditionalInformation(
                event.getEventAdditionalInformation().getDirector(),
                event.getEventAdditionalInformation().getWriterOrArtist(),
                event.getEventAdditionalInformation().getAuthor(),
                actorsList,
                tagList
        );
        ru.khokhlov.biletka.dto.response.EventWebWidget eventWebWidget = new ru.khokhlov.biletka.dto.response.EventWebWidget(
                event.getEventWebWidget().getSignature(),
                event.getEventWebWidget().getDescription(),
                event.getEventWebWidget().getRatingYandexAfisha(),
                event.getEventWebWidget().getLink()
        );

        return new EventDetail(eventBasicInformation, eventDuration, eventAdditionalInformation, eventWebWidget);
    }

    @Override
    public MassiveOfEvents getNearestEvents(String city) {
        log.trace("EventServiceImpl.getNearestEvents - city {}", city);

        return new MassiveOfEvents(
                sessionService.getMassiveDistinctEventByCityAndDates(
                        cityService.getCity(city).getCityId()));
    }

    @Override
    public MassivePublicEvents getAllEventByDate(String city, String placeName, LocalDate date) throws EntityNotFoundException {
        log.trace("PlaceServiceImpl.getAllEventByDate - city {}, name {}, date {}", city, placeName, date);

        Place place = placeService.getPlaceByNameAndCity(
                NameRebuilder.rebuildName(placeName),
                city
        );

        if (place == null) {
            throw new EntityNotFoundException(
                    "Place with city "
                            + city +
                            " and name "
                            + NameRebuilder.rebuildName(placeName)
                            + " does not exists!");
        }

        PublicEvent[] events = sessionService.getMassiveDistinctEventByPlaceAndDates(place.getId(), date);

        return new MassivePublicEvents(events);
    }

    @Override
    public Event getEventBySymbolicNameAndType(String eventSymbolicName, String eventType) throws EntityNotFoundException {
        log.trace("EventServiceImpl.getEventBySymbolicNameAndType - eventSymbolicName {}, eventType {}", eventSymbolicName, eventType);

        Event event = eventRepository.findFirstByEventTypeAndSymbolicName(
                eventTypeService.getEventType(eventType),
                eventSymbolicName);

        if (event == null) throw new EntityNotFoundException(
                "Event with symbolic name "
                        + eventSymbolicName +
                        " and type "
                        + eventType +
                        " not found!");
        return event;
    }

    @Override
    public Event getEventById(Long eventId) throws EntityNotFoundException {
        log.trace("EventServiceImpl.getEventById - eventId {}", eventId);
        return eventRepository.getReferenceById(eventId);
    }

    @Override
    public MassiveOfEvents getAllEvent() throws EntityNotFoundException {
        log.trace("EventServiceImpl.getAllEvent");

        List<Event> eventList = eventRepository.findAll();
        List<PublicEvent> publicEvents = new ArrayList<>();

        for (Event e : eventList) {
            publicEvents.add(
                    new PublicEvent(
                            e.getId(),
                            e.getEventBasicInformation().getName(),
                            e.getEventBasicInformation().getSymbolicName(),
                            e.getEventBasicInformation().getNameRus(),
                            e.getEventBasicInformation().getEventType().getType(),
                            String.format("%s%s", e.getEventDuration().getHours(), e.getEventDuration().getMinutes()),
                            e.getEventBasicInformation().getPushkin(),
                            e.getEventBasicInformation().getShowInPoster(),
                            e.getEventBasicInformation().getImg()
                    )
            );
        }

        return new MassiveOfEvents(publicEvents.toArray(PublicEvent[]::new));
    }

    @Override
    public PlacesAndInfoSession[] getPlacesAndInfo(String cityName, Long id) throws EntityNotFoundException {
        log.trace("EventServiceImpl.getPlacesAndInfo - cityName {}, id {}", cityName, id);

        Event event = eventRepository.getReferenceById(id);
        City city = cityService.getCity(cityName);

        List<Session> sessions = sessionService.getMassiveSessionByEventAndCity(city.getCityId(), event.getId());
        Set<Place> places = placeService.getAllByCity(city);

        Set<Place> placesSession = new HashSet<>();
        List<PlacesAndInfoSession> placesAndInfoSessions = new ArrayList<>();
        List<String> timing = new ArrayList<>();

        for (Place p : places) {
            for (Session s : sessions) {
                if (s.getPlace() == p) {
                    placesSession.add(p);

                    timing.add(
                            s.getStart().toLocalDateTime().toLocalTime().toString()
                    );
                }
            }

            if (!placesSession.isEmpty() && p == placesSession.stream().toList().get(placesSession.size() - 1)) {
                placesAndInfoSessions.add(
                        new PlacesAndInfoSession(
                                p.getId(),
                                p.getName(),
                                p.getAddress(),
                                timing.toArray(new String[0])
                        )
                );
            }

            timing.clear();
        }

        return placesAndInfoSessions.toArray(PlacesAndInfoSession[]::new);
    }

    @Override
    public EventResponse removeEventById(Long id) throws EntityNotFoundException {
        Event event = eventRepository.getReferenceById(id);
        EventResponse eventResponse = new EventResponse(id, event.getEventBasicInformation().getSymbolicName());

        additionalInformationService.deleteAdditionalInformation(event.getEventAdditionalInformation());
        basicInformationService.deleteBasicInformation(event.getEventBasicInformation());

        eventRepository.delete(event);
        return eventResponse;
    }

    @Override
    public List<Event> getAllFullInfo() {
        return eventRepository.findAll();
    }
}
