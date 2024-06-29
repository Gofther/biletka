package biletka.main.service.Impl;

import biletka.main.Utils.JwtTokenUtils;
import biletka.main.dto.request.OrganizationRegistrationRequest;
import biletka.main.dto.response.*;
import biletka.main.dto.response.TotalSession.PlacesByOrganization;
import biletka.main.dto.response.TotalSession.TotalSession;
import biletka.main.entity.*;
import biletka.main.enums.StatusUserEnum;
import biletka.main.repository.EventRepository;
import biletka.main.repository.OrganizationRepository;
import biletka.main.repository.SessionRepository;
import biletka.main.repository.TicketRepository;
import biletka.main.service.*;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Lazy))
@Slf4j
public class OrganizationServiceImpl implements OrganizationService {
    private final OrganizationRepository organizationRepository;
    private final JwtTokenUtils jwtTokenUtils;
    private final SessionService sessionService;
    @Lazy
    private final UserService userService;
    private final EventService eventService;
    private final SessionRepository sessionRepository;
    private final TicketRepository ticketRepository;

    private final HallService hallService;

    /**
     * Метод добавления организации в бд
     * @param organizationRequest данные организации
     * @param user данные о новом пользователе
     */
    @Override
    public void postCreateOrganization(OrganizationRegistrationRequest organizationRequest, Users user) {
        log.trace("OrganizationServiceImpl.postCreateOrganization - organizationRequest {}, user {}", organizationRequest, user);
        Organization organization = new Organization(
                user,
                organizationRequest.inn(),
                organizationRequest.kbk(),
                organizationRequest.kpp(),
                organizationRequest.ogrn(),
                organizationRequest.oktmo(),
                organizationRequest.contactPhone(),
                organizationRequest.email(),
                organizationRequest.fullNameOrganization(),
                organizationRequest.fullNameSignatory(),
                organizationRequest.legalAddress(),
                organizationRequest.namePayer(),
                organizationRequest.positionSignatory(),
                Integer.valueOf(organizationRequest.postalAddress()),
                new Timestamp(new Date().getTime()),
                StatusUserEnum.ACTIVE
        );

        organizationRepository.saveAndFlush(organization);
    }

    /**
     * Метод добавления мероприятия к организации
     * @param eventId id мероприятия
     * @param authorization токен авторизации
     * @return успешное создание записи
     */
    public MessageCreateResponse postEventOrganization(String authorization, Long eventId) {
        log.trace("OrganizationServiceImpl.postEventOrganization - eventId {}", eventId);
        Organization organization = tokenVerification(authorization);
        Event event = eventService.getEventById(eventId);
        if (organization.getEventSet().contains(event)) {
            throw new EntityExistsException("Event "+ eventId +" already added to organization");
        }
        organization.addEvent(event);
        organizationRepository.save(organization);

        return new MessageCreateResponse(
                "Event with id '" + eventId + "' added to organization"
        );
    }

    /**
     * Метод получения организации по данным
     * @param organizationRequest данные организации
     * @return организация
     */
    @Override
    public Organization getOrganizationByFullNameOrganization(OrganizationRegistrationRequest organizationRequest) {
        log.trace("OrganizationServiceImpl.getOrganizationByFullNameOrganization - organizationRequest {}", organizationRequest);
        return organizationRepository.findFirstByFullInfo(
                organizationRequest.inn(),
                organizationRequest.kbk(),
                organizationRequest.kpp(),
                organizationRequest.ogrn(),
                organizationRequest.oktmo(),
                organizationRequest.email(),
                organizationRequest.fullNameOrganization()
        );
    }

    /**
     * Метод получения организации по пользователю
     * @param user пользователь
     * @return организация
     */
    @Override
    public Organization getOrganizationByUser(Users user) {
        log.trace("OrganizationServiceImpl.getOrganizationByUser - user {}", user);
        return organizationRepository.findFirstByUser(user);
    }

    /**
     * Метод добавление площадки к организации
     * @param newPlace площадка
     */
    @Override
    public void addPlace(Organization organization, Place newPlace) {
        log.trace("OrganizationServiceImpl.addPlace - organization {}, newPlace {}", organization, newPlace);
        organization.addPlace(newPlace);

        organizationRepository.save(organization);
    }

//    /**
//     * Метод добавление мероприятия к организации
//     * @param event мероприятие
//     */
//    @Override
//    public void addEventAdmin(Organization organization, Event event) {
//        log.trace("OrganizationServiceImpl.addEventAdmin - organization {}, event {}", organization, event);
//        Set<Event> eventSet = organization.getAdminEventSet();
//        eventSet.add(event);
//
//        organization.addEvent(event);
//        organization.setAdminEventSet(eventSet);
//
//        organizationRepository.save(organization);
//    }



    /**
     * Метод получения мероприятий организации
     * @param authorization токен авторизации
     * @return массив мероприятий и их количесто
     */
    @Override
    public EventsOrganization getEventsOrganization(String authorization) {
        log.trace("OrganizationServiceImpl.getEventsOrganization - authorization {}", authorization);

        Organization organization = tokenVerification(authorization);

        ArrayList<EventOrganization> eventsOrganization = new ArrayList<>();

        organization.getEventSet().forEach(event -> {
            System.out.println(event);
            eventsOrganization.add(
                    new EventOrganization(
                        event.getId(),
                        event.getEventBasicInformation().getName_rus(),
                        event.getEventBasicInformation().getSymbolicName(),
                        event.getRating(),
                        event.getDuration(),
                        event.getEventBasicInformation().getPushkin(),
                        event.getEventAdditionalInformation().getTagSet().toArray(Tag[]::new),
                        event.getEventBasicInformation().getGenres().toArray(Genre[]::new),
                        String.valueOf(event.getEventBasicInformation().getAgeRatingId().getLimitation()),
                        sessionService.getTotalByEventAndPlaces(event, organization.getPlaceSet())
                )
            );
        });

        return new EventsOrganization(
                eventsOrganization.size(),
                eventsOrganization.toArray(EventOrganization[]::new)
        );
    }

    /**
     * Метод получения площадок организации
     * @param authorization токен пользователя
     * @return массив площадок
     */
    @Override
    public PlacesOrganization getPlacesOrganization(String authorization) {
        log.trace("OrganizationServiceImpl.getPlacesOrganization - authorization {}", authorization);

        Organization organization = tokenVerification(authorization);

        ArrayList<PlaceOrganization> placeOrganizationArrayList = new ArrayList<>();

        organization.getPlaceSet().forEach(place -> {
            placeOrganizationArrayList.add(
                    new PlaceOrganization(
                            place.getId(),
                            place.getAddress(),
                            place.getCity().getCityName(),
                            place.getPlaceName(),
                            hallService.getTotalByPlace(place)
                    )
            );
        });

        return new PlacesOrganization(
                placeOrganizationArrayList.size(),
                placeOrganizationArrayList.toArray(PlaceOrganization[]::new)
        );
    }

    /**
     * Метод получения залов у организации
     * @param authorization токе авторизации
     * @return массив залов
     */
    @Override
    public MassivePlacesAndHalls getPlacesAndSession(String authorization) {
        log.trace("OrganizationServiceImpl.getPlacesAndSession - authorization {}", authorization);
        Organization organization = tokenVerification(authorization);

        ArrayList<PlaceHallOrganization> placeHallOrganizationArrayList = new ArrayList<>();

        organization.getPlaceSet().forEach(place -> {
            ArrayList<HallOrganization> hallOrganizationArrayList = new ArrayList<>();

            hallService.getAllHallByPlace(place).forEach(hall -> {
                hallOrganizationArrayList.add(
                        new HallOrganization(
                                hall.getId(),
                                hall.getHallNumber(),
                                hall.getHallName(),
                                hall.getNumberOfSeats(),
                                hall.getInfo(),
                                hall.getScheme() == null
                        )
                );
            });

            placeHallOrganizationArrayList.add(
                    new PlaceHallOrganization(
                            place.getId(),
                            place.getPlaceName(),
                            place.getAddress(),
                            place.getCity().getCityName(),
                            hallOrganizationArrayList.toArray(HallOrganization[]::new)
                    )
            );
        });

        return new MassivePlacesAndHalls(placeHallOrganizationArrayList.toArray(PlaceHallOrganization[]::new));
    }

    /**
     * Проверка токена авторизации и вывод организации
     * @param token токен авторизации
     * @return организация
     */
    public Organization tokenVerification(String token) {
        log.trace("OrganizationServiceImpl.tokenVerification - token {}", token);
        String userEmail = jwtTokenUtils.getUsernameFromToken(
                token.substring(7)
        );

        Users user = userService.getUserOrganizationByEmail(userEmail);

        if (user == null) {
            throw new EntityNotFoundException("A broken token!");
        }

        Organization organization = getOrganizationByUser(user);

        if (organization == null) {
            throw new EntityNotFoundException("A broken token!");
        }

        return organization;
    }

    /**
     * Метод получения организации по токену
     * @param authorization - токен авторизации
     * @return организация
     */
    @Override
    public OrganizationResponse getOrganization(String authorization){
        log.trace("OrganizationServiceImpl.getAllOrganization - authorization {}", authorization);
        String userEmail = jwtTokenUtils.getUsernameFromToken(
                authorization.substring(7)
        );

        Users user = userService.getUserOrganizationByEmail(userEmail);

        if (user == null) {
            throw new EntityNotFoundException("A broken token!");
        }

        Organization organization = getOrganizationByUser(user);

        if (organization == null) {
            throw new EntityNotFoundException("A broken token!");
        }
        return new OrganizationResponse(
                organization.getId(),
                organization.getUser(),
                organization.getInn(),
                organization.getKbk(),
                organization.getKpp(),
                organization.getOgrn(),
                organization.getOktmo(),
                organization.getContactPhone(),
                organization.getEmail(),
                organization.getFullNameOrganization(),
                organization.getFullNameSignatory(),
                organization.getLegalAddress(),
                organization.getNamePayer(),
                organization.getPositionSignatory(),
                organization.getPostalAddress()
        );
    }

    /**
     * Метод получения сеансов по токену
     * @param authorization - токен авторизации
     * @return массив сеансов по площадкам и мероприятиям
     */
    @Override
    public TotalSession getSessionsByOrganization(String authorization){
        log.trace("OrganizationServiceImpl.getSessionsByOrganization - authorization {}", authorization);
        String userEmail = jwtTokenUtils.getUsernameFromToken(
                authorization.substring(7)
        );

        Users user = userService.getUserOrganizationByEmail(userEmail);

        if (user == null) {
            throw new EntityNotFoundException("A broken token!");
        }

        Organization organization = getOrganizationByUser(user);

        if (organization == null) {
            throw new EntityNotFoundException("A broken token!");
        }

        ArrayList<PlacesByOrganization> places = new ArrayList<>();

        Set<Place> placeSet = organization.getPlaceSet();

        placeSet.forEach(place -> {
            places.add(
                    new PlacesByOrganization(
                            place.getPlaceName(),
                            place.getCity().getCityName(),
                            place.getAddress(),
                            sessionService.getSessionByPlaceAndEvent(place)
                    )
            );
        });
        return new TotalSession(places.toArray(PlacesByOrganization[]::new));
    }

    /**
     * Метод получения статистики продаж за месяц
     * @param authorization токен авторизации
     * @return статистика продаж и возвратов
     */
    @Override
    public SalesResponse getMonthlySalesOrganization(String authorization) {
        log.trace("OrganizationServiceImpl.getMonthlySalesOrganization - authorization {}", authorization);
        Organization organization = tokenVerification(authorization);

        Calendar calendar = Calendar.getInstance();
        Timestamp finishDay = new Timestamp(calendar.getTimeInMillis());
        calendar.add(Calendar.MONTH, -1);
        Timestamp startDay = new Timestamp(calendar.getTimeInMillis());

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String timePeriod = sdf.format(startDay) + " -- " + sdf.format(finishDay);

        Integer sales = 0;
        Integer onSales = 0;
        Integer refunded = 0;
        Double salesPercent = 0.0;
        Double refundedPercent = 0.0;

        Set<Event> events = organization.getEventSet();
        ArrayList<Session> sessions = new ArrayList<>();
        for (Event event : events) {
            ArrayList<Session> eventSessions = sessionRepository.findAllSessionByEventAndDate(event, startDay, finishDay);
            sessions.addAll(eventSessions);
        }
        for (Session session : sessions) {
            if(session.getOnSales() == 0){
                continue;
            }
            sales += session.getSales();
            onSales += session.getOnSales();
            ArrayList<Ticket> tickets = ticketRepository.getAllBySession(session.getId());
            for(Ticket ticket : tickets) {
                if(ticket.getIsRefunded()){
                    refunded += 1;
                }
            }
        }
        if(onSales != 0) {
            salesPercent = roundToHundredths(((double) sales / onSales) * 100.0);
            refundedPercent = roundToHundredths(((double) refunded / onSales) * 100.0);
        }
        return new SalesResponse(
                timePeriod,
                onSales,
                sales,
                salesPercent,
                refunded,
                refundedPercent
        );
    }
    /**
     * Метод получения статистики продаж за месяц
     * @param authorization токен авторизации
     * @return статистика продаж и возвратов
     */
    @Override
    public YearlySalesResponse getYearlySalesOrganization(String authorization) {
        log.trace("OrganizationServiceImpl.getYearlySalesOrganization - authorization {}", authorization);
        Organization organization = tokenVerification(authorization);

        List<MonthlySalesResponse> monthlySales = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        int currentYear = calendar.get(Calendar.YEAR);

        for (int month = Calendar.JANUARY; month <= Calendar.DECEMBER; month++) {
            calendar.set(currentYear, month, 1, 0, 0, 0);
            Timestamp startDay = new Timestamp(calendar.getTimeInMillis());

            calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
            Timestamp finishDay = new Timestamp(calendar.getTimeInMillis());

            int sales = 0;
            int onSales = 0;

            Set<Event> events = organization.getEventSet();
            ArrayList<Session> sessions = new ArrayList<>();

            for (Event event : events) {
                ArrayList<Session> eventSessions = sessionRepository.findAllSessionByEventAndDate(event, startDay, finishDay);
                sessions.addAll(eventSessions);
            }

            for (Session session : sessions) {
                if (session.getOnSales() == 0) {
                    continue;
                }
                sales += session.getSales();
                onSales += session.getOnSales();
            }

            double salesPercent = onSales != 0 ? roundToHundredths(((double) sales / onSales) * 100.0) : 0.0;

            String monthName = new SimpleDateFormat("MMMM").format(startDay);
            monthlySales.add(new MonthlySalesResponse(
                    monthName,
                    onSales,
                    sales,
                    salesPercent
            ));
        }

        return new YearlySalesResponse(currentYear, monthlySales.toArray(new MonthlySalesResponse[0]));
    }


    private static double roundToHundredths(double value) {
        BigDecimal bigDecimal = BigDecimal.valueOf(value);
        BigDecimal roundedBigDecimal = bigDecimal.setScale(2, RoundingMode.HALF_UP);
        return roundedBigDecimal.doubleValue();
    }
}
