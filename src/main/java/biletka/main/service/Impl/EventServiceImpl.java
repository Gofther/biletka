package biletka.main.service.Impl;

import biletka.main.Utils.FileUtils;
import biletka.main.Utils.JwtTokenUtils;
import biletka.main.dto.request.EventCreateRequest;
import biletka.main.dto.response.MessageCreateResponse;
import biletka.main.dto.universal.MassivePublicEvent;
import biletka.main.dto.universal.PublicEvent;
import biletka.main.dto.universal.PublicEventImage;
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
import java.util.*;

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
     * @return массив краткой информации
     */
    @Override
    public MassivePublicEvent getEventLimit(String cityName, String authorization, Integer offset) {
        log.trace("EventServiceImpl.cityName - cityName {}", cityName);
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

        Set<Event> events = sessionService.getMassiveEventByCityLimit(city, offset);


        if (authorization != null) {
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
                                favoriteSet.contains(event)
                        )
                );
            });
        } else {
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
                                null
                        )
                );
            });
        }

        return new MassivePublicEvent(publicEvents.toArray(PublicEvent[]::new));
    }
}
