package biletka.main.service.Impl;

import biletka.main.Utils.ConvertUtils;
import biletka.main.Utils.FileUtils;
import biletka.main.Utils.JwtTokenUtils;
import biletka.main.dto.request.SessionCreateRequest;
import biletka.main.dto.response.HallScheme.SchemeFloor;
import biletka.main.dto.response.HallScheme.SchemeRow;
import biletka.main.dto.response.HallScheme.SchemeSeat;
import biletka.main.dto.response.HallSchemeResponse;
import biletka.main.dto.response.MessageCreateResponse;
import biletka.main.dto.universal.PublicHallFile;
import biletka.main.entity.*;
import biletka.main.exception.ErrorMessage;
import biletka.main.exception.InvalidDataException;
import biletka.main.repository.SessionRepository;
import biletka.main.repository.TicketRepository;
import biletka.main.service.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

@Service
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Lazy))
public class SessionServiceImpl implements SessionService {
    private final JwtTokenUtils jwtTokenUtils;
    private final SessionRepository sessionRepository;
    private final TicketRepository ticketRepository;
    private final ConvertUtils convertUtils;
    private final UserService userService;
    private final OrganizationService organizationService;
    private final FileUtils fileUtils;
    @Lazy
    private final EventService eventService;
    private final HallService hallService;
    private final TypeOfMovieService typeOfMovieService;


    /**
     * Метод создания и сохранения сеанса мероприятия в бд
     * @param authorization токен авторизации
     * @param sessionCreateRequest информация о сеансе
     * @return о успешном создании сеанса
     */
    @Override
    public MessageCreateResponse sessionCreate(String authorization, SessionCreateRequest sessionCreateRequest) {
        log.trace("SessionServiceImpl.sessionCreate - authorization {}, sessionCreateRequest {}", authorization, sessionCreateRequest);

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

        /** Проврека мероприятия на существование */
        Event event = eventService.getEventById(sessionCreateRequest.eventId());

        if (event == null) {
            List<ErrorMessage> errorMessages = new ArrayList<>();
            errorMessages.add(new ErrorMessage("Event error", "The event does not exist!"));
            throw new InvalidDataException(errorMessages);
        }
        System.out.println(1);
        /** Проврека зала на существование */
        Hall hall = hallService.getHallById(sessionCreateRequest.hallId());

        if (hall == null) {
            List<ErrorMessage> errorMessages = new ArrayList<>();
            errorMessages.add(new ErrorMessage("Hall error", "The hall does not exist!"));
            throw new InvalidDataException(errorMessages);
        }
        System.out.println(2);

        /** Проврека времени */
        LocalDateTime start = sessionCreateRequest.startTime();
        LocalDateTime finish = start.plusHours(Long.parseLong(event.getDuration().split(":")[0]))
                .plusMinutes(Long.parseLong(event.getDuration().split(":")[1]));

        if (LocalDateTime.now().isAfter(start)) {
            List<ErrorMessage> errorMessages = new ArrayList<>();
            errorMessages.add(new ErrorMessage("Start time error", "The date and time of the start of the event must be in the future!"));
            throw new InvalidDataException(errorMessages);
        }
        System.out.println(3);

        /** Получения типа сеанса */
        TypeOfMovie typeOfMovie = typeOfMovieService.getTypeByName(sessionCreateRequest.typeOfMovie());

        System.out.println(4);
        /** Проврерка на существование сеанса */
        Session[] sessions = sessionRepository.findSessionsByInfo(Timestamp.valueOf(sessionCreateRequest.startTime()), Timestamp.valueOf(finish), hall.getId());

        System.out.println(5);
        if (sessions.length != 0) {
            List<ErrorMessage> errorMessages = new ArrayList<>();
            errorMessages.add(new ErrorMessage("Session error", "It is impossible to create a session, because the hall does not exist, or it is occupied!"));
            throw new InvalidDataException(errorMessages);
        }

        System.out.println(6);
        /** Сохранение сеанса */
        Session session = new Session(
                0,
                hall.getNumberOfSeats(),
                Timestamp.valueOf(start),
                Timestamp.valueOf(finish),
                0,
                sessionCreateRequest.price(),
                true,
                event,
                hall,
                typeOfMovie
        );

        sessionRepository.saveAndFlush(session);

        System.out.println(8);
        return new MessageCreateResponse(
                "The session '" + event.getEventBasicInformation().getName() +"' in the hall '" + hall.getHallName() + "' has been successfully created!"
        );
    }

    /**
     * Метод получения уникальных мероприятий по сеансам
     * @param city город
     * @param offset отступ
     * @param date дата для выборки
     * @return массив мероприятий
     */
    @Override
    public Set<Event> getMassiveEventByCityLimit(City city, Integer offset, Date date) {
        log.trace("SessionServiceImpl.getMassiveEventByCityLimit - city {}, offset {}", city, offset);
        return sessionRepository.findAllEventByCity(city, offset, new Timestamp(date.getTime()));
    }

    /**
     * Метод получения уникальных мероприятий по сеансам и дате создания мероприятия
     * @param city город
     * @param offset отступ
     * @param date дата для выборки
     * @return массив мероприятий
     */
    @Override
    public Set<Event> getMassiveAnnouncementByCityLimit(City city, Integer offset, Date date) {
        log.trace("SessionServiceImpl.getMassiveAnnouncementByCityLimit - city {}, offset {}", city, offset);
        return sessionRepository.findAllEventAdvertisementByCity(city, offset, new Timestamp(date.getTime()), new Timestamp(date.getTime() - 1000000000));
    }

    /**
     * Метод получения сеансов мероприятия по городу и дате
     * @param event мероприятие
     * @param city город
     * @param date дата для поиска
     * @return массив сеансов
     */
    @Override
    public ArrayList<Session> getSessionsByEvent(Event event, City city, Date date) {
        log.trace("SessionServiceImpl.getSessionsByEvent - event {}, city {}, date {}", event, city, date);
        return sessionRepository.findAllSessionByEventAndCity(
                event,
                city,
                Timestamp.valueOf(date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate().atStartOfDay()),
                Timestamp.valueOf(date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate().atTime(LocalTime.MAX)));
    }

    /**
     * Получение количества сеансов мероприятия организации
     * @param event мероприятие
     * @param placeSet площадки
     * @return количество сеансов
     */
    @Override
    public Integer getTotalByEventAndPlaces(Event event, Set<Place> placeSet) {
        log.trace("SessionServiceImpl.getTotalByEventAndPlaces - event {}, placeSet {}", event, placeSet);
        AtomicReference<Integer> total = new AtomicReference<>(0);

        placeSet.forEach(place -> {
            total.updateAndGet(v -> v + sessionRepository.findSumByEventAndPlace(event, place));
        });

        return total.get();
    }

    /**
     * Метод получения сеансов мероприятия id
     * @param sessionId id сеанса
     * @return массив сеансов
     */
    @Override
    public Session getSessionById(Long sessionId) {
        log.trace("SessionServiceImpl.getSessionsById - id {}", sessionId);
        return sessionRepository.findSessionById(sessionId);
    }



    /**
     * Получение схемы зала по сессии
     *
     * @param authorization токен авторизации пользователя
     * @param sessionId     id сессии
     * @return схема зала
     */
    @Override
    public HallSchemeResponse getSessionHallScheme(String authorization, Long sessionId) throws IOException{

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
        Session session = getSessionById(sessionId);
        Hall hall = session.getHall();
        String schemeText = hall.getScheme();
        HallSchemeResponse scheme = null;
        try {
            scheme = convertUtils.convertToJSONSchemeCreate(schemeText);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        String filename = String.format("%d-%s-%d.svg", hall.getId(), hall.getHallName(), hall.getHallNumber());
        PublicHallFile file = fileUtils.getFileHall(filename);

        SchemeFloor[] updatedFloors = new SchemeFloor[scheme.schemeFloors().length];
        for (int i = 0; i < scheme.schemeFloors().length; i++) {
            SchemeFloor floor = scheme.schemeFloors()[i];
            SchemeRow[] updatedRows = new SchemeRow[floor.schemeRows().length];
            for (int j = 0; j < floor.schemeRows().length; j++) {
                SchemeRow row = floor.schemeRows()[j];
                SchemeSeat[] updatedSeats = new SchemeSeat[row.schemeSeats().length];
                for (int k = 0; k < row.schemeSeats().length; k++) {
                    SchemeSeat seat = row.schemeSeats()[k];
                    boolean isOccupied = ticketRepository.getFirstBySessionAndRowAndSeat(sessionId, Integer.parseInt(row.rowNumber()), Integer.parseInt(seat.number())) != null;
                    updatedSeats[k] = seat.withOccupied(isOccupied);
                }
                updatedRows[j] = row.withSchemeSeats(updatedSeats);
            }
            updatedFloors[i] = floor.withSchemeRows(updatedRows);
        }

        return new HallSchemeResponse(
                updatedFloors,
                file
        );
    }
}
