package biletka.main.service.Impl;

import biletka.main.Utils.FileUtils;
import biletka.main.Utils.JwtTokenUtils;
import biletka.main.dto.request.EventCreateRequest;
import biletka.main.dto.response.EventResponse;
import biletka.main.dto.response.MessageCreateResponse;
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
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@Service
@RequiredArgsConstructor
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
        System.out.println(typeFile);
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
     * @param id - id Мероприятия
     * @return Мероприятие
     */
@Override
    public EventResponse getEventOfId(Long id){
        log.trace("EventServiceImpl.GetEventOfId - id {}", id);
        Event event = eventRepository.getReferenceById(id);
        if (event == null) {
            List<ErrorMessage> errorMessages = new ArrayList<>();
            errorMessages.add(new ErrorMessage("Event error", "This event does not exists!"));
            throw new InvalidDataException(errorMessages);
        }
        if (event.getEventBasicInformation().getShowInPoster() == null){
            List<ErrorMessage> errorMessages = new ArrayList<>();
            errorMessages.add(new ErrorMessage("Event error", "ShowInPoster() == null"));
            throw new InvalidDataException(errorMessages);
        }

        //Конвертация из HashSet в String[]
        String[] genres = new String[event.getEventBasicInformation().getGenres().size()];
        int i = 0;
        for (Genre g : event.getEventBasicInformation().getGenres()) {
            String genre = g.getName().toString();
            genres[i] = genre;
            i++;
        }
        i=0;
        String[] tags = new String[event.getEventAdditionalInformation().getTagSet().size()];
        for (Tag t : event.getEventAdditionalInformation().getTagSet()) {
            String tag = t.getName().toString();
            tags[i] = tag;
            i++;
        }
    return new EventResponse(event.getId(),
                event.getEventBasicInformation().getSymbolicName(),
                event.getEventBasicInformation().getName_rus(),
                event.getRating(),
                event.getEventWebWidget().getDescription(),
                event.getEventBasicInformation().getTypeEventId().getType(),
                event.getEventBasicInformation().getAgeRatingId().getLimitation(),
                genres,
                event.getEventAdditionalInformation().getAuthor(),
                event.getEventAdditionalInformation().getWriterOrArtist(),
                event.getDuration(),
                tags,
                event.getEventBasicInformation().getPushkin());

    }

    /**
     * Метод получения мероприятия по id и названию
     * @param name - id и название через -
     * @return Мероприятие
     */

@Override
    public EventResponse getEventOfName(String name){
    // Проверка введенных данных
        String regex = "[0-9]{0,}+[-]+[A-Za-z0-9\\-]{0,}";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(name);

        if (!matcher.matches()){
            List<ErrorMessage> errorMessages = new ArrayList<>();
            errorMessages.add(new ErrorMessage("Event error", "incorrect data form"));
            throw new InvalidDataException(errorMessages);
        }

        int pos = name.indexOf("-");
        Long id = Long.parseLong(name.substring(0,pos));
        name = name.substring(pos+1,name.length());

        Event event = eventRepository.findFirstById(id);

        if (event == null) {
            List<ErrorMessage> errorMessages = new ArrayList<>();
            errorMessages.add(new ErrorMessage("Event error", "This event does not exists!"));
            throw new InvalidDataException(errorMessages);
        }

        if(!Objects.equals(event.getEventBasicInformation().getSymbolicName(), name)){
            List<ErrorMessage> errorMessages = new ArrayList<>();
            errorMessages.add(new ErrorMessage("Event error", "This event does not exists!"));
            throw new InvalidDataException(errorMessages);
        }

        //Конвертация из HashSet в String[]
        String[] genres = new String[event.getEventBasicInformation().getGenres().size()];
        int i = 0;
        for (Genre g : event.getEventBasicInformation().getGenres()) {
            String genre = g.getName().toString();
            genres[i] = genre;
            i++;
        }
        i=0;
        String[] tags = new String[event.getEventAdditionalInformation().getTagSet().size()];
        for (Tag t : event.getEventAdditionalInformation().getTagSet()) {
            String tag = t.getName().toString();
            tags[i] = tag;
            i++;
        }
        return new EventResponse(event.getId(),
                event.getEventBasicInformation().getSymbolicName(),
                event.getEventBasicInformation().getName_rus(),
                event.getRating(),
                event.getEventWebWidget().getDescription(),
                event.getEventBasicInformation().getTypeEventId().getType(),
                event.getEventBasicInformation().getAgeRatingId().getLimitation(),
                genres,
                event.getEventAdditionalInformation().getAuthor(),
                event.getEventAdditionalInformation().getWriterOrArtist(),
                event.getDuration(),
                tags,
                event.getEventBasicInformation().getPushkin());
        }
}
