package ru.khokhlov.biletka.service.impl;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import ru.khokhlov.biletka.dto.request.TicketEditInfo;
import ru.khokhlov.biletka.dto.request.TicketInfo;
import ru.khokhlov.biletka.dto.response.SessionInfo;
import ru.khokhlov.biletka.dto.response.TicketsMassiveResponse;
import ru.khokhlov.biletka.dto.response.TicketsResponse;
import ru.khokhlov.biletka.dto.response.TicketsSessionResponse;
import ru.khokhlov.biletka.entity.Event;
import ru.khokhlov.biletka.entity.Place;
import ru.khokhlov.biletka.entity.Session;
import ru.khokhlov.biletka.entity.TicketsInfo;
import ru.khokhlov.biletka.repository.TicketRepository;
import ru.khokhlov.biletka.service.SessionService;
import ru.khokhlov.biletka.service.TicketService;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Lazy))
@Slf4j
public class TicketServiceImpl implements TicketService {
    private final SessionService sessionService;
    private final TicketRepository ticketRepository;

    @Override
    public TicketsResponse createTicket(TicketInfo ticketInfo) throws EntityNotFoundException {
        log.trace("TicketServiceImpl.createTicket - ticketInfo {}", ticketInfo);
        try {
            Session session = sessionService.getSessionById(ticketInfo.sessionId());

            TicketsInfo ticketsInfo = new TicketsInfo(
                    ticketInfo.price(),
                    0,
                    ticketInfo.onSale(),
                    true
            );

            ticketsInfo.setSession(session);

            ticketRepository.saveAndFlush(ticketsInfo);

            return new TicketsResponse(
                    ticketsInfo.getId(),
                    ticketsInfo.getPrice(),
                    ticketsInfo.getOnSales(),
                    ticketsInfo.getSales()
            );
        }
        catch (RuntimeException ex){
            throw new EntityNotFoundException("Session with id " + ticketInfo.sessionId() + " not found");
        }
    }

    @Override
    public TicketsResponse editTicket(TicketEditInfo ticketEditInfo) throws EntityNotFoundException, EntityExistsException {
        log.trace("TicketServiceImpl.editTicket - ticketEditInfo {}", ticketEditInfo);

        TicketsInfo ticketsInfo = ticketRepository.getReferenceById(ticketEditInfo.id());

        if (ticketsInfo.getSales().equals(ticketEditInfo.sales()) && ticketsInfo.getOnSales().equals(ticketEditInfo.onSale()) && ticketsInfo.getPrice().equals(ticketEditInfo.price()))
            throw new EntityExistsException("Ticket with sales " + ticketsInfo.getSales() + " and " + ticketsInfo.getOnSales() + " already exist!");

        ticketRepository.editTicket(ticketEditInfo.id(), ticketEditInfo.price(), ticketEditInfo.sales(), ticketEditInfo.onSale());

        return new TicketsResponse(
                ticketEditInfo.id(),
                ticketEditInfo.price(),
                ticketEditInfo.onSale(),
                ticketEditInfo.sales()
        );
    }

    @Override
    public TicketsMassiveResponse getAllTickets() {
        log.trace("TicketServiceImpl.getAllTickets");
        return countingTickets(ticketRepository.findAll());
    }

    @Transactional
    @Override
    public void deleteAllTicketsBySession(Session session) {
        log.trace("TicketServiceImpl.deleteAllTicketsBySession - session {}", session);
        ticketRepository.deleteAllBySessionId(session.getId());
    }

    @Transactional
    @Override
    public void deleteAllTicketsByEventAndPlace(Event event, Place place) {
        log.trace("TicketServiceImpl.deleteAllTicketsByEventAndPlace - eventSymbolicName {}, place {}", event, place);
        ticketRepository.deleteAllBySession_EventAndSession_Place(event.getId(), place.getId());
    }

    @Override
    public SessionInfo editSellSession(Session session, Boolean status) throws EntityNotFoundException {
        log.trace("TicketServiceImpl.editSellSession - session {}, status {}", session, status);

        Event event = session.getEvent();
        List<TicketsSessionResponse> ticketsResponseList = new ArrayList<>();
        List<TicketsInfo> ticketsInfoList = ticketRepository.findAllBySession(session.getId());

        if (ticketsInfoList == null)
            throw new EntityNotFoundException("Ticket with session_id " + session.getId() + " not exist!");

        SessionInfo response = new SessionInfo(
                session.getId(),
                event.getEventBasicInformation().getName(),
                event.getEventBasicInformation().getNameRus(),
                session.getStart().toLocalDateTime().toLocalDate(),
                session.getStart().toLocalDateTime().toLocalTime(),
                Integer.toString(session.getRoomLayout().getHallNumber()),
                Boolean.TRUE.equals(ticketsInfoList.get(0).getStatus()) ? "В продаже" : "Продажи закрыты",
                ticketsResponseList);

        for (TicketsInfo ticketsInfo : ticketsInfoList) {
            ticketsInfo.setStatus(status);
            ticketRepository.saveAndFlush(ticketsInfo);

            ticketsResponseList.add(
                    new TicketsSessionResponse(
                            ticketsInfo.getId(),
                            ticketsInfo.getOnSales() + ticketsInfo.getSales(),
                            ticketsInfo.getPrice(),
                            ticketsInfo.getOnSales(),
                            ticketsInfo.getSales()
                    )
            );
        }

        log.trace("TicketServiceImpl.editSellSession - Session response {}", response);
        return response;
    }

    @Override
    public SessionInfo getInfoTicketsAndSession(Session session) {
        log.trace("TicketServiceImpl.getInfoTicketsAndSession - Session {}", session);

        SessionInfo response;
        List<TicketsSessionResponse> ticketsResponseList = new ArrayList<>();
        List<TicketsInfo> ticketsInfoList = ticketRepository.findAllBySession(session.getId());

        if(ticketsInfoList == null)
            throw new EntityNotFoundException("Ticket with session_id " + session.getId() + " not exist!");

        if (ticketsInfoList.isEmpty()) {
            ticketsResponseList.add(new TicketsSessionResponse(
                            0L,
                            null,
                            null,
                            null,
                            null
                    )
            );
            response = new SessionInfo(
                    session.getId(),
                    session.getEvent().getEventBasicInformation().getName(),
                    session.getEvent().getEventBasicInformation().getNameRus(),
                    session.getStart().toLocalDateTime().toLocalDate(),
                    session.getStart().toLocalDateTime().toLocalTime(),
                    Integer.toString(session.getRoomLayout().getHallNumber()),
                    "Продажи закрыты",
                    ticketsResponseList
            );
        } else {
            for (TicketsInfo ticketsInfo : ticketsInfoList) {
                ticketsResponseList.add(
                        new TicketsSessionResponse(
                                ticketsInfo.getId(),
                                ticketsInfo.getOnSales() + ticketsInfo.getSales(),
                                ticketsInfo.getPrice(),
                                ticketsInfo.getOnSales(),
                                ticketsInfo.getSales()
                        )
                );
            }
            String status = "Продажи закрыты";

            //TODO подумать как избежать try/catch
            try {
                if(Boolean.TRUE.equals(ticketsInfoList.get(0).getStatus()))
                    status = "В продаже";
            }
            catch (IndexOutOfBoundsException ex){
                log.warn("List is empty");
            }

            response = new SessionInfo(
                    session.getId(),
                    session.getEvent().getEventBasicInformation().getName(),
                    session.getEvent().getEventBasicInformation().getNameRus(),
                    session.getStart().toLocalDateTime().toLocalDate(),
                    session.getStart().toLocalDateTime().toLocalTime(),
                    Integer.toString(session.getRoomLayout().getHallNumber()),
                    status,
                    ticketsResponseList
            );
        }
        return response;
    }

    @Override
    public List<TicketsInfo> getAllTicketByPlace(Place place) {
        log.trace("TicketServiceImpl.getAllTicketByPlace - place {}", place);
        return ticketRepository.getAllTicketByOrganization(place);
    }

    private TicketsMassiveResponse countingTickets(List<TicketsInfo> ticketsInfoList) {
        log.debug("TicketServiceImpl.countingTickets - Ticket {}", ticketsInfoList);

        int total = 0;
        int price = 0;
        int onSale = 0;
        int sales = 0;
        List<TicketsResponse> ticketsResponseList = new ArrayList<>();

        for (TicketsInfo t : ticketsInfoList) {
            if (Boolean.TRUE.equals(t.getStatus())) {
                total++;
                price += t.getPrice();
                onSale += t.getOnSales();
                sales += t.getSales();

                ticketsResponseList.add(
                        new TicketsResponse(
                                t.getId(),
                                t.getPrice(),
                                t.getOnSales(),
                                t.getSales()
                        )
                );
            }
        }

        return new TicketsMassiveResponse(
                total,
                price,
                onSale,
                sales,
                ticketsResponseList.toArray(TicketsResponse[]::new)
        );
    }
}
