package biletka.main.service.Impl;

import biletka.main.Utils.JwtTokenUtils;
import biletka.main.dto.request.SessionCreateRequest;
import biletka.main.dto.response.MessageCreateResponse;
import biletka.main.entity.*;
import biletka.main.exception.ErrorMessage;
import biletka.main.exception.InvalidDataException;
import biletka.main.repository.SessionRepository;
import biletka.main.service.*;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Service
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Lazy))
public class SessionServiceImpl implements SessionService {
    private final JwtTokenUtils jwtTokenUtils;
    private final SessionRepository sessionRepository;

    private final UserService userService;
    private final OrganizationService organizationService;
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

        /** Проврека зала на существование */
        Hall hall = hallService.getHallById(sessionCreateRequest.hallId());

        if (hall == null) {
            List<ErrorMessage> errorMessages = new ArrayList<>();
            errorMessages.add(new ErrorMessage("Hall error", "The hall does not exist!"));
            throw new InvalidDataException(errorMessages);
        }

        /** Проврека времени */
        LocalDateTime start = sessionCreateRequest.startTime();
        LocalDateTime finish = start.plusHours(Long.parseLong(event.getDuration().split(":")[0]))
                .plusMinutes(Long.parseLong(event.getDuration().split(":")[1]));

        if (LocalDateTime.now().isAfter(start)) {
            List<ErrorMessage> errorMessages = new ArrayList<>();
            errorMessages.add(new ErrorMessage("Start time error", "The date and time of the start of the event must be in the future!"));
            throw new InvalidDataException(errorMessages);
        }

        /** Получения типа сеанса */
        TypeOfMovie typeOfMovie = typeOfMovieService.getTypeByName(sessionCreateRequest.typeOfMovie());

        /** Проврерка на существование сеанса */
        Session[] sessions = sessionRepository.findSessionsByInfo(Timestamp.valueOf(sessionCreateRequest.startTime()), Timestamp.valueOf(finish), hall.getId());

        if (sessions.length != 0) {
            List<ErrorMessage> errorMessages = new ArrayList<>();
            errorMessages.add(new ErrorMessage("Session error", "It is impossible to create a session, because the hall does not exist, or it is occupied!"));
            throw new InvalidDataException(errorMessages);
        }

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
     * Метод получения уникальных мероприятий по возрасту
     * @param city город
     * @param age возраст
     * @param offset отступ
     * @param date дата для выборки
     * @return массив мероприятий
     */
     @Override
    public Set<Event> getMassiveEventByCityAndAgeLimit(City city, int age, Integer offset, Date date) {
        log.trace("SessionServiceImpl.getMassiveEventByCityLimit - city {}, age {}, offset {}", city, age, offset);
        return sessionRepository.findAllEventByCityAndAge(city, age, offset, new Timestamp(date.getTime()));
    }

    /**
     * Метод получения уникальных мероприятий по возрасту
     * @param city город
     * @param type тип мероприятия
     * @param offset отступ
     * @param date дата для выборки
     * @return массив мероприятий
     */
    @Override
    public Set<Event> getMassiveEventByCityAndType(City city, String type, Integer offset, Date date) {
        log.trace("SessionServiceImpl.getMassiveEventByCityLimit - city {}, type {}, offset {}", city, type, offset);
        return sessionRepository.findAllEventByCityAndType(city, type, offset, new Timestamp(date.getTime()));
    }


    @Override
    public Set<Event> getMassiveEventByCityAndGenre(City city, Genre genre, Integer offset, Date date) {
        log.trace("SessionServiceImpl.getMassiveEventByCityLimit - city {}, genre {}, offset {}", city, genre, offset);
        return sessionRepository.findAllEventByCityAndGenre(city, genre, offset, new Timestamp(date.getTime()));
    }

}
