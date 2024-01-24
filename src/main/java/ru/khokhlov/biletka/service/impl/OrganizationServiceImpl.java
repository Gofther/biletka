package ru.khokhlov.biletka.service.impl;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.khokhlov.biletka.dto.request.OrganizationAddEvent;
import ru.khokhlov.biletka.dto.request.OrganizationAddPlace;
import ru.khokhlov.biletka.dto.request.OrganizationRegistration;
import ru.khokhlov.biletka.dto.response.*;
import ru.khokhlov.biletka.dto.universal.*;
import ru.khokhlov.biletka.entity.*;
import ru.khokhlov.biletka.enums.RoleEnum;
import ru.khokhlov.biletka.repository.FileOrganizationRepository;
import ru.khokhlov.biletka.repository.EventImageRepository;
import ru.khokhlov.biletka.repository.OrganizationRepository;
import ru.khokhlov.biletka.repository.TicketRepository;
import ru.khokhlov.biletka.service.*;
import ru.khokhlov.biletka.utils.PasswordEncoder;

import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrganizationServiceImpl implements OrganizationService {
    private final OrganizationRepository organizationRepository;
    private final PlaceService placeService;
    private final SessionService sessionService;
    private final MailSender mailSender;
    private final EventService eventService;
    private final TicketService ticketService;
    private final TicketRepository ticketRepository;
    private final FileOrganizationRepository fileOrganizationRepository;

    private final EventImageRepository eventImageRepository;
    @Override
    public Long getOrganizationIdByEmailAndPassword(String email, String password) {
        Long id = -1L;
        Organization organization = organizationRepository.findByEmail(email);

        if (organization != null && PasswordEncoder.arePasswordsEquals(password, organization.getPassword()))
            id = organization.getId();

        return id;
    }

    @Override
    public OrganizationResponse createOrganization(OrganizationRegistration organizationRegistration) throws EntityExistsException {
        log.trace("OrganizationServiceImpl.createOrganization - organizationRegistration {}", organizationRegistration);

        String fullNameOrganization = organizationRegistration.fullNameOrganization();
        String email = organizationRegistration.email();

        if (organizationRepository.findFirstByFullNameOrganizationOrEmail(fullNameOrganization, email) != null) {
            throw new EntityExistsException("Entity with email: " + email + " or full name: " + fullNameOrganization + " already exists");
        }

        //TODO Mapper
        Organization organization = new Organization(
                organizationRegistration.fullNameOrganization(),
                organizationRegistration.legalAddress(),
                organizationRegistration.postalAddress(),
                organizationRegistration.contactPhone(),
                organizationRegistration.email(),
                UUID.randomUUID().toString(),
                organizationRegistration.fullNameSignatory(),
                organizationRegistration.positionSignatory(),
                organizationRegistration.documentContract(),
                organizationRegistration.INN(),
                organizationRegistration.KPP(),
                organizationRegistration.OGRN(),
                organizationRegistration.OKTMO(),
                organizationRegistration.KBK(),
                organizationRegistration.namePayer(),
                PasswordEncoder.getEncryptedPassword(organizationRegistration.password())
        );

        organization.setRoleEnum(RoleEnum.ORGANIZATION);
        organizationRepository.saveAndFlush(organization);
        organization.setPassword(organizationRegistration.password());

        mailSender.activateEmailOrganization(organization);
        return createOrganizationResponse(organization);
    }

    @Override
    public Organization getOrganizationById(Long organizationId) throws EntityNotFoundException {
        log.trace("OrganizationServiceImpl.getOrganizationById - organizationId {}", organizationId);

        return organizationRepository.getReferenceById(organizationId);
    }

    @Override
    public Organization getOrganizationByEmail(String email) {
        log.trace("OrganizationServiceImpl.getOrganizationByEmail - email {}", email);

        return organizationRepository.findByEmail(email);
    }

    @Override
    public OrganizationResponse addPlaceInOrganization(OrganizationAddPlace organizationAddPlace) throws EntityNotFoundException {
        log.trace("OrganizationServiceImpl.addPlaceByOrganization - organizationAddPlace {}", organizationAddPlace);

        Organization organization = organizationRepository.getReferenceById(organizationAddPlace.organizationId());

        for (String place : organizationAddPlace.places()) {
            Place placeNew = placeService.getPlaceByNameAndCity(place, organizationAddPlace.city());

            // TODO: 19.10.2023 Переписать логику
            if (placeNew != null) {
                organization.addPlaceSet(placeNew);
            } else throw new EntityNotFoundException(
                    "Place with city "
                            + organizationAddPlace.city() +
                            " and name "
                            + place
                            + " does not exists!");
        }

        organizationRepository.saveAndFlush(organization);

        return createOrganizationResponse(organization);
    }

    @Override
    public MassivePublicSessions getAllSession(Long organisationId) throws EntityNotFoundException {
        log.trace("OrganizationServiceImpl.getAllSession - getListFromOrganization {}", organisationId);

        Organization organization = organizationRepository.getReferenceById(organisationId);

        Set<Place> places = organization.getPlaceSet();
        List<PublicSession> sessions = new ArrayList<>();

        for (Place place : places) {
            List<Session> massiveSession = sessionService.getMassiveByPlace(place.getId());

            for (Session session : massiveSession) {
                if (session.getEvent() != null) {
                    Integer numberSeats = session.getRoomLayout() == null ? 0 : session.getRoomLayout().getNumberSeats();

                    List<TicketsInfo> ticketsInfoList = ticketRepository.findAllBySession(session.getId());

                    String status = "Продажи закрыты";

                    //TODO проблема с отображением статуса билетов
                    try {
                        if (Boolean.TRUE.equals(ticketsInfoList.get(0).getStatus()))
                            status = "В продаже";
                        else
                            status = "Продажи закрыты";

                    } catch (IndexOutOfBoundsException exception) {
                        log.warn("Ticket list is empty: " + ticketsInfoList);
                    }

                    sessions.add(
                            // TODO: 19.10.2023 Mapper
                            new PublicSession(
                                    session.getId(),
                                    session.getEvent().getEventBasicInformation().getName(),
                                    session.getEvent().getEventBasicInformation().getSymbolicName(),
                                    session.getStart().toLocalDateTime(),
                                    numberSeats,
                                    status
                            )
                    );
                }
            }
        }
        return new MassivePublicSessions(sessions);
    }

    @Override
    public MassivePublicEvents getAllEvent(Long organisationId) throws EntityNotFoundException {
        log.trace("OrganizationServiceImpl.getAllEvent - getListFromOrganization {}", organisationId);

        Organization organization = organizationRepository.getReferenceById(organisationId);

        List<PublicEvent> publicEventList = new ArrayList<>();

        for (Event event : organization.getEventSet()) {
            publicEventList.add(
                    // TODO: 19.10.2023 Mapper
                    new PublicEvent(
                            event.getId(),
                            event.getEventBasicInformation().getName(),
                            event.getEventBasicInformation().getSymbolicName(),
                            event.getEventBasicInformation().getNameRus(),
                            event.getEventBasicInformation().getEventType().getType(),
                            String.format("%s%s", event.getEventDuration().getHours(), event.getEventDuration().getMinutes()),
                            event.getEventBasicInformation().getPushkin(),
                            event.getEventBasicInformation().getShowInPoster(),
                            event.getEventBasicInformation().getImg()
                    )
            );
        }

        return new MassivePublicEvents(publicEventList.toArray(PublicEvent[]::new));
    }


    @Override
    public boolean isOrganizationActivate(String code) {
        Organization organization = organizationRepository.findByActivationCode(code);

        if (organization == null) {
            throw new EntityNotFoundException("This code is not exists!");
        }

        organization.setActivationCode(null);
        organizationRepository.saveAndFlush(organization);

        return true;
    }

    @Override
    public OrganizationResponse addEventInOrganization(OrganizationAddEvent organizationAddEvent) throws EntityNotFoundException {
        log.trace("OrganizationServiceImpl.addEventInOrganization - organizationAddEvent {}", organizationAddEvent);

        Organization organization = organizationRepository.getReferenceById(organizationAddEvent.organizationId());
        Event event = eventService.getEventById(organizationAddEvent.eventId());

        if (event == null) {
            throw new EntityNotFoundException(
                    "Event id "
                            + organizationAddEvent.eventId()
                            + " and organization id "
                            + organizationAddEvent.organizationId()
                            + " does not exists!"
            );
        }

        organization.addEventSet(event);
        organizationRepository.saveAndFlush(organization);

        return createOrganizationResponse(organization);
    }

    @Override
    public PublicOrganization infoOrganization(Integer userId) {
        Organization organization = organizationRepository.getReferenceById(Long.valueOf(userId));
        return new PublicOrganization(
                organization.getId(),
                organization.getFullNameOrganization(),
                organization.getLegalAddress(),
                String.valueOf(organization.getPostalAddress()),
                organization.getContactPhone(),
                organization.getEmail(),
                organization.getFullNameSignatory(),
                organization.getPositionSignatory(),
                String.valueOf(organization.getINN()),
                String.valueOf(organization.getKPP()),
                String.valueOf(organization.getOGRN()),
                String.valueOf(organization.getOKTMO()),
                String.valueOf(organization.getKBK())
        );
    }

    @Override
    public Event[] getUnevents(Long organizationId) {
        Organization organization = organizationRepository.getReferenceById(organizationId);
        List<Event> events = eventService.getAllFullInfo();
        List<Event> newEvents = new ArrayList<>();

        for (Event event: events) {
            if (!organization.getEventSet().contains(event)) {
                newEvents.add(event);
            }
        }

        return newEvents.toArray(Event[]::new);
    }

    @Override
    public void addFileInOrganization(Long id, Long fileId) {
        FileOrganization fileOrganization = fileOrganizationRepository.getReferenceById(fileId);
        Organization organization = organizationRepository.getReferenceById(id);

        organization.setFileOrganization(fileOrganization);

        organizationRepository.saveAndFlush(organization);
    }

    @Override
    public DeleteEventOrganization deleteEventOrganizationRelation(Long organisationId, Long eventId) throws EntityNotFoundException {
        log.trace("OrganizationServiceImpl.deleteEvent - organisationId {}, eventId {}", organisationId, eventId);

        Event event = eventService.getEventById(eventId);
        Organization organization = organizationRepository.getReferenceById(organisationId);

        DeleteEventOrganization deleteEventOrganization = new DeleteEventOrganization(sessionService.deleteAllSessionByEventAndPlaces(event, organization.getPlaceSet().toArray(Place[]::new)));

        organization.getEventSet().remove(event);

        organizationRepository.saveAndFlush(organization);

        return deleteEventOrganization;
    }

    @Override
    public DeleteSession deleteSession(Long organisationId, Long sessionId) throws EntityNotFoundException {
        log.trace("OrganizationServiceImpl.deleteSession - organisationId {}, sessionId {}", organisationId, sessionId);

        Organization organization = organizationRepository.getReferenceById(organisationId);

        // TODO: 19.10.2023 Как не отсылать null?
        if (organization.getPlaceSet().toArray(Place[]::new).length == 0)
            return null;

        return sessionService.deleteSessionById(sessionId);
    }

    @Override
    public SessionInfo editSellSession(Long organisationId, Long sessionId, Boolean status) throws EntityNotFoundException {
        log.trace("OrganizationServiceImpl.editSellSession - organisationId {}, sessionId {}, status {}", organisationId, sessionId, status);

        Organization organization = organizationRepository.getReferenceById(organisationId);
        Session session = sessionService.getSessionById(sessionId);

        // TODO: 19.10.2023 Как не отсылать null?
        SessionInfo sessionInfos = null;

        for (Place place : organization.getPlaceSet()) {
            if (place.equals(session.getPlace())) {
                sessionInfos = ticketService.editSellSession(session, status);
            }
        }

        return sessionInfos;
    }

    @Override
    public SessionInfo getSession(Long organisationId, Long sessionId) throws EntityNotFoundException {
        log.trace("OrganizationServiceImpl.getSession - organisationId {}, sessionId {}", organisationId, sessionId);

        Organization organization = organizationRepository.getReferenceById(organisationId);
        Session session = sessionService.getSessionById(sessionId);

        if (session == null || organization.getPlaceSet().isEmpty())
            throw new EntityNotFoundException();

        SessionInfo sessionInfos = null;

        for (Place place : organization.getPlaceSet()) {
            if (place.equals(session.getPlace())) {
                sessionInfos = ticketService.getInfoTicketsAndSession(session);
            }
        }

        return sessionInfos;
    }

    // TODO: 19.10.2023 Mapper
    private OrganizationResponse createOrganizationResponse(Organization organization) {
        return new OrganizationResponse(
                organization.getId(),
                organization.getFullNameOrganization(),
                organization.getLegalAddress(),
                organization.getPostalAddress(),
                organization.getContactPhone(),
                organization.getEmail(),
                organization.getActivationCode(),
                organization.getFullNameSignatory(),
                organization.getPositionSignatory(),
                organization.getDocumentContract(),
                organization.getINN(),
                organization.getKPP(),
                organization.getOGRN(),
                organization.getOKTMO(),
                organization.getKBK(),
                organization.getNamePayer(),
                organization.getPassword(),
                new ArrayList<>(organization.getPlaceSet()),
                new ArrayList<>(organization.getEventSet())
        );
    }
}
