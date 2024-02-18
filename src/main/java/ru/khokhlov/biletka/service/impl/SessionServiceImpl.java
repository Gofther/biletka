package ru.khokhlov.biletka.service.impl;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import ru.khokhlov.biletka.dto.request.SessionInfo;
import ru.khokhlov.biletka.dto.request.TicketInfo;
import ru.khokhlov.biletka.dto.response.*;
import ru.khokhlov.biletka.dto.universal.PublicEvent;
import ru.khokhlov.biletka.dto.universal.PublicSession;
import ru.khokhlov.biletka.entity.*;
import ru.khokhlov.biletka.repository.CityRepository;
import ru.khokhlov.biletka.repository.SessionRepository;
import ru.khokhlov.biletka.repository.TicketRepository;
import ru.khokhlov.biletka.service.*;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Lazy))
@Slf4j
public class SessionServiceImpl implements SessionService {
    private static final Integer WEEK = 7;
    private final SessionRepository sessionRepository;
    private final PlaceService placeService;
    private final MovieViewTypeService movieViewTypeService;
    @Lazy
    private final EventService eventService;
    private final HallSchemeService hallSchemeService;
    private final TicketService ticketService;
    private final OrganizationService organizationService;
    private final TicketRepository ticketRepository;
    private final CityRepository cityRepository;

    @Override
    public String[] getMassiveDistinctDatesByEventAndCity(Long eventId, Long cityId) {
        log.trace("SessionServiceImpl.getMassiveDistinctDatesByEventAndCity - eventId {}, cityId {}", eventId, cityId);

        List<Session> sessions = sessionRepository.findAllByEventIdAndPlaceCity_CityId(eventId, cityId);
        List<String> date = new ArrayList<>();

        for (Session session : sessions) {
            date.add(session.getStart().toLocalDateTime().toLocalDate().format(DateTimeFormatter.ofPattern("d.MM.uuuu")));
        }

        return date.toArray(String[]::new);
    }

    @Override
    public PublicEvent[] getMassiveDistinctEventByPlaceAndDates(Long placeId, LocalDate date) {
        log.trace("SessionServiceImpl.getMassiveDistinctEventByPlaceAndDates - placeId {}, date {}", placeId, date);

        List<Session> sessions = sessionRepository.findAllByPlaceId(placeId);
        List<PublicEvent> eventList = new ArrayList<>();

        LocalDate finish = date.plusDays(WEEK);

        for (Session s : sessions) {
            addPublicEvent(eventList, date, finish, s);
        }
        return eventList.toArray(PublicEvent[]::new);
    }


    @Override
    public PublicEvent[] getMassiveDistinctEventByCityAndDates(Long cityId) {
        log.trace("SessionServiceImpl.getMassiveDistinctEventByCityAndDates - cityId {}", cityId);

        List<Session> sessionList = sessionRepository.findAllByPlaceCity_CityId(cityId);
        List<PublicEvent> eventList = new ArrayList<>();

        LocalDate start = LocalDate.now();
        LocalDate finish = LocalDate.now().plusDays(WEEK);

        for (Session session : sessionList) {
            addPublicEvent(eventList, start, finish, session);
        }
        return eventList.toArray(PublicEvent[]::new);
    }

    /**
     * Добавления мероприятия по определенным параметрам
     *
     * @param eventList лист мероприятий
     * @param start дата начала
     * @param finish дата окончания
     * @param session сессия
     */
    private void addPublicEvent(List<PublicEvent> eventList, LocalDate start, LocalDate finish, Session session) {
        LocalDate dateToCheck = session.getStart().toLocalDateTime().toLocalDate();

        if ((start.isBefore(dateToCheck) && finish.isAfter(dateToCheck))
                || start.isEqual(dateToCheck)
                || finish.isEqual(dateToCheck)) {
            Event event = session.getEvent();

            eventList.add(
                    new PublicEvent(
                            event.getId(),
                            event.getEventBasicInformation().getName(),
                            event.getEventBasicInformation().getSymbolicName(),
                            event.getEventBasicInformation().getNameRus(),
                            event.getEventBasicInformation().getEventType().getType(),
                            String.format("%s:%s", event.getEventDuration().getHours(), event.getEventDuration().getMinutes()),
                            event.getEventBasicInformation().getPushkin(),
                            event.getEventBasicInformation().getShowInPoster(),
                            event.getEventBasicInformation().getImg(),
                            event.getEventWebWidget().getLink()
                    )
            );
        }
    }

    @Override
    public SessionResponse createSession(String city, SessionInfo sessionInfo) throws EntityNotFoundException {
        log.trace("SessionServiceImpl.createSession - city {}, sessionInfo {}", city, sessionInfo);

        MovieViewType movieViewType = movieViewTypeService.getFirstByType(sessionInfo.typeOfMovie());
        Event event = eventService.getEventBySymbolicNameAndType(sessionInfo.eventSymbolicName(), sessionInfo.eventType());
        HallScheme hallScheme = hallSchemeService.getHallScheme(sessionInfo.hallSchemeId());

        Place place = hallScheme.getPlace();

        if (movieViewType == null) {
            movieViewType = movieViewTypeService.createMovieViewType(sessionInfo.typeOfMovie());
        }

        Session session = new Session(
                Timestamp.valueOf(sessionInfo.datetime())
        );

        session.setTypeOfMovie(movieViewType);
        session.setEvent(event);
        session.setPlace(place);
        session.setRoomLayout(hallScheme);

        sessionRepository.saveAndFlush(session);

        TicketInfo ticketInfo = new TicketInfo(
                session.getId(),
                sessionInfo.basicPrice(),
                hallScheme.getNumberSeats()
        );

        TicketsResponse ticketsResponse = ticketService.createTicket(
                ticketInfo
        );

        return new SessionResponse(
                session.getId(),
                session.getEvent().getEventBasicInformation().getName(),
                session.getEvent().getEventBasicInformation().getSymbolicName(),
                session.getEvent().getEventBasicInformation().getEventType().getType(),
                session.getRoomLayout().getHallNumber()
        );
    }

    /**
     * Функция получения массива сессий мероприятий по городу и фильтру
     *
     * @param cityName  название города
     * @param date      Дата
     * @param placeName Место
     * @return массив сессий мероприятий
     */
    @Override
    public PublicSession[] getMassiveBySessionFilter(LocalDate date, String placeName, String cityName) {
        log.trace("SessionServiceImpl.getMassiveBySessionFilter - date {}, placeName {}, cityName {}", date, placeName, cityName);

        if (date == null) {
            date = LocalDate.now();
        }

        List<PublicSession> sessionsResponseList = new ArrayList<>();
        List<Session> sessionList = sessionRepository.findAllByStartAndPlaceCity_CityId(
                date,
                placeService.getPlaceByNameAndCity(placeName, cityName).getId()
        );

        for (Session s : sessionList) {
            sessionsResponseList.add(
                    new PublicSession(
                            s.getId(),
                            s.getEvent().getEventBasicInformation().getName(),
                            s.getEvent().getEventBasicInformation().getSymbolicName(),
                            s.getStart().toLocalDateTime(),
                            s.getRoomLayout().getNumberSeats(),
                            ticketRepository.findAllBySession(s.getId()) != null && ticketRepository.findAllBySession(s.getId()).get(0).getStatus() ? "В продаже" : "Продажи закрыты"
                    )
            );
        }

        return sessionsResponseList.toArray(PublicSession[]::new);
    }

    /**
     * Функция получения массива сессий мероприятий по площадкам
     *
     * @param placeId id площадки
     * @return массив сессий
     */
    @Override
    public List<Session> getMassiveByPlace(Long placeId) {
        log.trace("SessionServiceImpl.getMassiveByPlace - placeId {}", placeId);

        return sessionRepository.findAllByPlaceId(placeId);
    }

    @Override
    public SessionResponseByTicket[] getMassiveByEvent(Long eventId, String city, LocalDate date){
        log.trace("SessionServiceImpl.getMassiveByEventCityDate - eventId {} , city {}, date {}", eventId,city,date);
        List<SessionResponseByTicket> sessionsResponseList = new ArrayList<>();
        Long cityId = cityRepository.findFirstByNameEng(city).getCityId();
        List<Session> sessionList = sessionRepository.findAllByEventIdDateAndCityId(eventId,cityId, date);

        for (Session s : sessionList) {
            List<TicketsInfo> ticketsInfos = ticketRepository.findAllBySession(s.getId());
            for (TicketsInfo t : ticketsInfos ) {
                sessionsResponseList.add(
                        new SessionResponseByTicket(
                        s.getId(),
                        s.getEvent().getEventBasicInformation().getName(),
                        s.getEvent().getEventBasicInformation().getSymbolicName(),
                        s.getTypeOfMovie(),
                        s.getStart().toLocalDateTime(),
                        s.getRoomLayout().getHallNumber(),
                        t.getPrice()
                    )
            );
            }
        }
        return sessionsResponseList.toArray(SessionResponseByTicket[]::new);
    }

    /**
     * Функция удаления всех сессий ивента
     *
     * @param event  информация ивента
     * @param places массив площадок
     * @return возвращает название удаленного ивента
     */
    @Transactional
    @Override
    public String deleteAllSessionByEventAndPlaces(Event event, Place[] places) {
        log.trace("SessionServiceImpl.deleteAllSessionByEventAndPlaces - eventSymbolicName {}, places {}", event, places);

        String eventName = event.getEventBasicInformation().getName();

        for (Place p : places) {
            ticketService.deleteAllTicketsByEventAndPlace(event, p);
            sessionRepository.deleteAllByEventAndPlace(event.getId(), p.getId());
        }

        return eventName;
    }

    /**
     * Функция удаления сессии по id
     *
     * @param sessionId id сессии
     * @return возвращает успешное/неуспешное удаление сессии
     */
    @Transactional
    @Override
    public DeleteSession deleteSessionById(Long sessionId) throws EntityNotFoundException {
        log.trace("SessionServiceImpl.deleteSessionById - sessionId {}", sessionId);

        Session session = sessionRepository.getReferenceById(sessionId);

        DeleteSession deleteSession = new DeleteSession(
                session.getEvent().getEventBasicInformation().getName(),
                session.getStart().toLocalDateTime());

        ticketService.deleteAllTicketsBySession(session);
        sessionRepository.deleteSession(session.getId());

        return deleteSession;
    }

    /**
     * Функция получения сессии по id
     *
     * @param sessionId id сессии
     * @return возвращает успешное/неуспешное получение сессии
     */
    @Override
    public Session getSessionById(Long sessionId) throws EntityNotFoundException {
        log.trace("SessionServiceImpl.getFirstBySession - sessionId {}", sessionId);
        return sessionRepository.getReferenceById(sessionId);
    }

    /**
     * Функция поиска сессий по информации
     *
     * @param city           название города
     * @param organizationId информация виджета для поиска
     * @return массив сессий
     */
    @Override
    public SessionWidgetResponse[] getMassiveByWidget(String city, Long organizationId) {
        log.trace("SessionServiceImpl.getMassiveByWidget - city {}, organizationId {}", city, organizationId);

        Organization organization = organizationService.getOrganizationById(organizationId);
        List<SessionWidgetResponse> ticketList = new ArrayList<>();

        if (organization == null || organization.getPlaceSet().isEmpty())
            return new SessionWidgetResponse[0];

        for (Place p : organization.getPlaceSet()) {
            List<TicketsInfo> foundTicketsInfos = ticketService.getAllTicketByPlace(p);

            for (TicketsInfo ticketsInfo : foundTicketsInfos) {
                ticketList.add(
                        new SessionWidgetResponse(
                                ticketsInfo.getId(),
                                ticketsInfo.getSession().getEvent().getEventBasicInformation().getName(),
                                ticketsInfo.getSession().getEvent().getEventBasicInformation().getSymbolicName(),
                                ticketsInfo.getSession().getStart().toLocalDateTime().toLocalTime(),
                                ticketsInfo.getSession().getStart().toLocalDateTime().toLocalDate(),
                                Integer.toString(ticketsInfo.getSession().getRoomLayout().getHallNumber()),
                                ticketsInfo.getOnSales(),
                                ticketsInfo.getPrice()
                        )
                );
            }
        }

        return ticketList.toArray(SessionWidgetResponse[]::new);
    }

    @Override
    public List<Session> getMassiveSessionByEventAndCity(Long city, Long event) throws EntityNotFoundException {
        log.trace("SessionServiceImpl.getMassiveSessionByEventAndCity - city {}, event {}", city, event);
        return sessionRepository.findAllByEventIdAndPlaceCity_CityId(event, city);
    }

}
