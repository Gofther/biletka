package biletka.main.service.Impl;

import biletka.main.Utils.FileUtils;
import biletka.main.Utils.JwtTokenUtils;
import biletka.main.dto.request.EventCreateRequest;
import biletka.main.dto.response.MessageCreateResponse;
import biletka.main.dto.universal.*;
import biletka.main.entity.*;
import biletka.main.entity.event_item.EventAdditionalInformation;
import biletka.main.entity.event_item.EventBasicInformation;
import biletka.main.entity.event_item.EventWebWidget;
import biletka.main.enums.StatusEventEnum;
import biletka.main.exception.ErrorMessage;
import biletka.main.exception.InvalidDataException;
import biletka.main.repository.EventRepository;
import biletka.main.service.*;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Lazy))
@Slf4j
public class EventServiceImpl implements EventService {
    private final FileUtils fileUtils;
    private final JwtTokenUtils jwtTokenUtils;
    private final EventRepository eventRepository;

    private final EventBasicInformationService eventBasicInformationService;
    private final EventAdditionalInformationService eventAdditionalInformationService;
    private final EventWebWidgetService eventWebWidgetService;
    private final UserService userService;
    private final OrganizationService organizationService;
    private final CityService cityService;
    private final ClientService clientService;
    @Lazy
    private final SessionService sessionService;
    private final RatingService ratingService;

    /**
     * Метод создания и добавления мероприятия в бд
     * @param authorization токен авторизации
     * @param file изображение мероприятия
     * @param eventCreateRequest информация мероприятия
     * @return сообщение оуспешном создании мероприятия
     */
    @Override
    public MessageCreateResponse createEvent(String authorization, MultipartFile file, EventCreateRequest eventCreateRequest) throws IOException {
        log.trace("EventServiceImpl.createEvent - authorization {}, file {}, eventCreateRequest {}", authorization, file.getOriginalFilename(), eventCreateRequest);
        /** Проверка на типа файла */
        String typeFile = fileUtils.getFileExtension(file.getOriginalFilename());

        fileUtils.validationFile(
                typeFile,
                new String[]{"png", "jpg"}
        );

        /**  Проверка на организацию пользователя*/
        String userEmail = jwtTokenUtils.getUsernameFromToken(
                authorization.substring(7)
        );

        Users user = userService.getUserOrganizationByEmail(userEmail);

        if (user == null) {
            throw new EntityNotFoundException("A broken token!");
        }

        Organization organization = organizationService.getOrganizationByUser(user);

        if (organization == null) {
            throw new EntityNotFoundException("A broken token!");
        }

        /** Проверка на существование мероприятия */
        Event event = eventRepository.findFirstByName(eventCreateRequest.eventBasicRequest().name(), eventCreateRequest.eventBasicRequest().typeEvent());

        if (event != null) {
            List<ErrorMessage> errorMessages = new ArrayList<>();
            errorMessages.add(new ErrorMessage("Create event error", "This event already exists!"));
            throw new InvalidDataException(errorMessages);
        }

        /** Создание дочерних элементов */
        EventBasicInformation eventBasicInformation = eventBasicInformationService.createEventBasic(eventCreateRequest.eventBasicRequest(), file.getOriginalFilename());
        EventAdditionalInformation eventAdditionalInformation = eventAdditionalInformationService.createEventAdditional(eventCreateRequest.eventAdditional());
        EventWebWidget eventWebWidget = eventWebWidgetService.createEventWebWidget(eventCreateRequest.webWidget());

        Event eventNew = new Event(
                eventWebWidget,
                eventAdditionalInformation,
                eventBasicInformation,
                eventCreateRequest.duration(),
                0.0,
                new Timestamp(new Date().getTime()),
                StatusEventEnum.ACTIVE
        );

        eventRepository.saveAndFlush(eventNew);

        /** Добавление мероприятия к организации */
        organizationService.addEventAdmin(organization, eventNew);

        /** Сохранение файла */
        fileUtils.fileUpload(file, "event/" + eventNew.getId() + "-" + eventNew.getEventBasicInformation().getSymbolicName() + "." + typeFile);

        return new MessageCreateResponse(
                "The event '" + eventNew.getEventBasicInformation().getName() + "' of the '" + eventNew.getEventBasicInformation().getTypeEventId().getType() + "' type has been successfully created!"
        );
    }

    /**
     * Метод получения мероприятия по id
     * @param id мероприятия
     * @return мероприятие
     */
    @Override
    public Event getEventById(Long id) {
        log.trace("EventServiceImpl.getEventById - id {}", id);
        return eventRepository.getReferenceById(id);
    }

    /**
     * Метод получения изображения мероприятия
     * @param id мероприятия
     * @param symbolicName символичное название мероприятия
     * @return данные для изображения
     */
    @Override
    public PublicEventImage getImageEvent(String id, String symbolicName) throws EntityNotFoundException, IOException {
        log.trace("EventServiceImpl.getImageEvent - id {}, symbolicName {}", id, symbolicName);
        Event event = eventRepository.findFirstByIdAndSymbolicName(Long.valueOf(id), symbolicName);

        if (event == null) {
            throw new EntityNotFoundException("The event not found!");
        }

        return fileUtils.getFileEvent(event.getEventBasicInformation().getImg());
    }

    /**
     * Метод получения кртакой информации о 10 мероприятиях по городу
     * @param cityName название города
     * @param authorization токен авторизации
     * @param offset отсчет мероприятий
     * @param date дата для выборки
     * @return массив краткой информации
     */
    @Override
    public MassivePublicEvent getEventLimit(String cityName, String authorization, Integer offset, Date date) {
        log.trace("EventServiceImpl.cityName - cityName {}, offset {}, date {}", cityName, offset, date);
        City city = cityService.getCityByNameEng(cityName);

        Set<Event> favoriteSet = new HashSet<>();
        ArrayList<PublicEvent> publicEvents= new ArrayList<>();

        /**  Проверка на пользователя*/
        if (authorization != null) {
            String userEmail = jwtTokenUtils.getUsernameFromToken(
                    authorization.substring(7)
            );

            Users user = userService.getUserByEmail(userEmail);

            if (user == null) {
                throw new EntityNotFoundException("A broken token!");
            }

            Client client = clientService.getClientByUser(user);

            favoriteSet.addAll(client.getEventSet());
        }

        Set<Event> events = sessionService.getMassiveEventByCityLimit(city, offset, date);

        events.forEach(event -> {
            Set<String> genres = new HashSet<>();

            event.getEventBasicInformation().getGenres().forEach(genre -> genres.add(genre.getName()));

            publicEvents.add(
                    new PublicEvent(
                            event.getId(),
                            event.getEventBasicInformation().getName_rus(),
                            event.getEventBasicInformation().getSymbolicName(),
                            event.getEventBasicInformation().getAgeRatingId().getLimitation(),
                            genres.toArray(String[]::new),
                            event.getEventBasicInformation().getImg(),
                            event.getEventBasicInformation().getTypeEventId().getType(),
                            authorization == null ? null : favoriteSet.contains(event)
                    )
            );
        });

        return new MassivePublicEvent(publicEvents.toArray(PublicEvent[]::new));
    }

    /**
     * Метод получения анонсов 10 мероприятий по городу
     * @param cityName название города
     * @param authorization токен авторизации
     * @param offset отсчет мероприятий
     * @return массив краткой информации
     */
    @Override
    public MassivePublicEvent getAnnouncementLimit(String cityName, String authorization, Integer offset, Date date) {
        log.trace("EventServiceImpl.getAnnouncementLimit - cityName {}, offset {}, date {}", cityName, offset, date);
        City city = cityService.getCityByNameEng(cityName);

        Set<Event> favoriteSet = new HashSet<>();
        ArrayList<PublicEvent> publicEvents = new ArrayList<>();

        /**  Проверка на пользователя*/
        if (authorization != null) {
            String userEmail = jwtTokenUtils.getUsernameFromToken(
                    authorization.substring(7)
            );

            Users user = userService.getUserByEmail(userEmail);

            if (user == null) {
                throw new EntityNotFoundException("A broken token!");
            }

            Client client = clientService.getClientByUser(user);

            favoriteSet.addAll(client.getEventSet());
        }

        Set<Event> events = sessionService.getMassiveAnnouncementByCityLimit(city, offset, date);

        events.forEach(event -> {
            Set<String> genres = new HashSet<>();

            event.getEventBasicInformation().getGenres().forEach(genre -> genres.add(genre.getName()));

            publicEvents.add(
                    new PublicEvent(
                            event.getId(),
                            event.getEventBasicInformation().getName_rus(),
                            event.getEventBasicInformation().getSymbolicName(),
                            event.getEventBasicInformation().getAgeRatingId().getLimitation(),
                            genres.toArray(String[]::new),
                            event.getEventBasicInformation().getImg(),
                            event.getEventBasicInformation().getTypeEventId().getType(),
                            authorization == null ? null : favoriteSet.contains(event)
                    )
            );
        });

        return new MassivePublicEvent(publicEvents.toArray(PublicEvent[]::new));
    }

    /**
     * Метод получения полной информации меропрития и места проведения и его сеансов
     * @param authorization токе авторизации
     * @param cityName название города
     * @param eventName id и символьное название мероприятия
     * @param date дата для поиска
     * @return полная информация мероприятия
     */
    @Override
    public PublicFullInfoEvent getFullInfoEvent(String authorization, String cityName, String eventName, Date date) throws EntityNotFoundException {
        log.trace("EventServiceImpl.getFullInfoEvent - cityName {}, event {}, date {}", cityName, eventName, date);
        City city = cityService.getCityByNameEng(cityName);

        Set<Event> favoriteSet = new HashSet<>();

        /**  Проверка на пользователя*/
        if (authorization != null) {
            String userEmail = jwtTokenUtils.getUsernameFromToken(
                    authorization.substring(7)
            );

            Users user = userService.getUserByEmail(userEmail);

            if (user == null) {
                throw new EntityNotFoundException("A broken token!");
            }

            Client client = clientService.getClientByUser(user);

            favoriteSet.addAll(client.getEventSet());
        }

        String[] eventStringName = eventName.split("-", 2);

        if (!Pattern.compile("^\\d+$").matcher(eventStringName[0]).matches() &&
                !Pattern.compile("^[A-Za-z0-9._%+@-]+$").matcher(eventStringName[1]).matches()
        ) {
            List<ErrorMessage> errorMessages = new ArrayList<>();
            errorMessages.add(new ErrorMessage("Event error", "The event line was entered incorrectly!"));
            throw new InvalidDataException(errorMessages);
        }

        Event event = eventRepository.findFirstByIdAndSymbolicName(Long.valueOf(eventStringName[0]), eventStringName[1]);

        if (event == null) {
            throw new EntityNotFoundException("There is no such event!");
        }

        ArrayList<MassiveSessionEvent> massiveSessionEvents = new ArrayList<>();

        ArrayList<Session> sessions = sessionService.getSessionsByEvent(event, city, date);

        if (!sessions.isEmpty()) {
            ArrayList<Place> places = new ArrayList<>();
            ArrayList<PublicSession> publicSessions = new ArrayList<>();

            Session lastSession = sessions.get(sessions.size() - 1);

            sessions.forEach(session -> {
                PublicSession publicSession = new PublicSession(
                        session.getId(),
                        session.getPrice(),
                        session.getStartTime().toLocalDateTime(),
                        (session.getOnSales() != 0) && (!LocalDateTime.now().isAfter(session.getStartTime().toLocalDateTime()))
                );

                if (lastSession == session) {
                    publicSessions.add(
                            publicSession
                    );

                    massiveSessionEvents.add(
                            new MassiveSessionEvent(
                                    session.getHall().getPlace().getPlaceName(),
                                    session.getHall().getPlace().getAddress(),
                                    publicSessions.toArray(PublicSession[]::new)
                            )
                    );
                } else if (!places.contains(session.getHall().getPlace())) {
                    if (!publicSessions.isEmpty()) {
                        massiveSessionEvents.add(
                                new MassiveSessionEvent(
                                        places.get(places.size() - 1).getPlaceName(),
                                        places.get(places.size() - 1).getAddress(),
                                        publicSessions.toArray(PublicSession[]::new)
                                )
                        );

                        publicSessions.clear();
                    }

                    places.add(session.getHall().getPlace());
                }

                publicSessions.add(publicSession);
            });
        }

        ArrayList<String> genres = new ArrayList<>();
        ArrayList<String> actors = new ArrayList<>();
        ArrayList<String> tags = new ArrayList<>();

        event.getEventBasicInformation().getGenres().forEach(genre -> genres.add(genre.getName()));
        event.getEventAdditionalInformation().getActorSet().forEach(actor -> actors.add(actor.getName()));
        event.getEventAdditionalInformation().getTagSet().forEach(tag -> tags.add(tag.getName()));

        return new PublicFullInfoEvent(
                event.getId(),
                event.getEventBasicInformation().getName_rus(),
                event.getEventBasicInformation().getSymbolicName(),
                event.getEventWebWidget().getDescription(),
                event.getDuration(),
                event.getRating(),
                event.getEventBasicInformation().getAgeRatingId().getLimitation(),
                genres.toArray(String[]::new),
                event.getEventBasicInformation().getPushkin(),
                event.getEventBasicInformation().getImg(),
                event.getEventBasicInformation().getTypeEventId().getType(),
                event.getEventAdditionalInformation().getWriterOrArtist(),
                actors.toArray(String[]::new),
                tags.toArray(String[]::new),
                authorization == null ? null : favoriteSet.contains(event),
                massiveSessionEvents.toArray(MassiveSessionEvent[]::new)
        );
    }

    /**
     * Метод получения мероприятия по id и символьному названию
     * @param eventSymbolic id и символьное название
     * @return мероприятие
     */
    @Override
    public Event getEventByIdAndSymbolic(String eventSymbolic) {
        log.trace("EventServiceImpl.getEventByIdAndSymbolic - eventSymbolic {}", eventSymbolic);

        String[] eventStringName = eventSymbolic.split("-", 2);

        if (!Pattern.compile("^\\d+$").matcher(eventStringName[0]).matches() &&
                !Pattern.compile("^[A-Za-z0-9._%+@-]+$").matcher(eventStringName[1]).matches()
        ) {
            List<ErrorMessage> errorMessages = new ArrayList<>();
            errorMessages.add(new ErrorMessage("Event error", "The event line was entered incorrectly!"));
            throw new InvalidDataException(errorMessages);
        }

        Event event = eventRepository.findFirstByIdAndSymbolicName(Long.valueOf(eventStringName[0]), eventStringName[1]);

        if (event == null) {
            throw new EntityNotFoundException("There is no such event!");
        }

        return event;
    }

    /**
     * Метод изменения рейтинга мероприятия
     * @param event мероприятие
     * @param rating рейтинг пользователя
     */
    @Override
    public void putRatingEvent(Event event, Double rating) {
        log.trace("EventServiceImpl.putRatingEvent - event {}", event);
        Double totalRating = ratingService.getTotalRatingByEvent(event);

        event.setRating(
                (event.getRating() + rating) / totalRating
        );
        eventRepository.save(event);
    }
}
