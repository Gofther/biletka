package ru.khokhlov.biletka.service.impl;

import com.google.zxing.WriterException;
import jakarta.mail.MessagingException;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import ru.khokhlov.biletka.dto.request.*;
import ru.khokhlov.biletka.dto.response.*;
import ru.khokhlov.biletka.dto.response.SessionInfo;
import ru.khokhlov.biletka.dto.response.ticketsOrganization_full.TicketOrganization;
import ru.khokhlov.biletka.dto.response.ticketsOrganization_full.TicketsSessionOrganization;
import ru.khokhlov.biletka.entity.*;
import ru.khokhlov.biletka.exception.ErrorMessage;
import ru.khokhlov.biletka.exception.InvalidDataException;
import ru.khokhlov.biletka.repository.ClientRepository;
import ru.khokhlov.biletka.repository.TicketRepository;
import ru.khokhlov.biletka.repository.TicketUserRepository;
import ru.khokhlov.biletka.service.*;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Lazy))
@Slf4j
public class TicketServiceImpl implements TicketService {
    private final SessionService sessionService;
    private final TicketRepository ticketRepository;
    private final ClientRepository clientRepository;
    private final TicketUserRepository ticketUserRepository;
    private final MailSender mailSender;
    private final HallSchemeService hallSchemeService;
    private final OrganizationService organizationService;

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

    @Override
    public List<TicketUserResponse> getTicketsByUser(Long userId) {
        log.trace("TicketServiceImpl.getTicketsByUser , userId - {}", userId);

        Set<Ticket> basketList = clientRepository.getReferenceById(userId).getBasket();
        List<TicketUserResponse> ticketUserResponses = new ArrayList<>();

        for (Ticket ticket : basketList) {
            ticketUserResponses.add(
                    new TicketUserResponse(
                            ticket.getId(),
                            ticket.getInfo().getSession().getEvent().getEventBasicInformation().getNameRus(),
                            ticket.getInfo().getSession().getPlace().getName(),
                            ticket.getInfo().getSession().getPlace().getAddress(),
                            ticket.getInfo().getSession().getStart().toString(),
                            ticket.getInfo().getSession().getRoomLayout().getName(),
                            ticket.getRowNumber(),
                            ticket.getSeatNumber(),
                            ticket.getPrice(),
                            ticket.getEmail(),
                            ticket.getPhone(),
                            ticket.getFullName()
                    )
            );
        }
        return ticketUserResponses;
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

    @Transactional
    @Override
    public TicketUserResponse postBuyTicket(BuyRequest buyRequest) {
        TicketsInfo ticketsInfo = ticketRepository.findFirstBySessionId(buyRequest.idSession());

        if(ticketsInfo == null) {
            throw new EntityNotFoundException("Tickets with session_id " + buyRequest.idSession() + " not exist!");
        }

        if(ticketsInfo.getOnSales() == 0) {
            List<ErrorMessage> errorMessages = new ArrayList<>();
            errorMessages.add(new ErrorMessage("There are no tickets", "Tickets for the session are sold out!"));
            throw new InvalidDataException(errorMessages);
        }

        if(ticketUserRepository.findFirstByRowNumberAndSeatNumber(buyRequest.rawNumber(), buyRequest.seatNumber()) != null) {
            List<ErrorMessage> errorMessages = new ArrayList<>();
            errorMessages.add(new ErrorMessage("The place is already occupied", "The place is already occupied for the event!"));
            throw new InvalidDataException(errorMessages);
        }

        if(hallSchemeService.getSeatScheme(ticketsInfo.getSession().getRoomLayout().getId(), buyRequest.rawNumber(), buyRequest.seatNumber())) {
            List<ErrorMessage> errorMessages = new ArrayList<>();
            errorMessages.add(new ErrorMessage("There is no place", "This place or row does not exist!"));
            throw new InvalidDataException(errorMessages);
        }

        ticketRepository.buyOneTicket(ticketsInfo.getId());

        Ticket ticket = new Ticket(
            buyRequest.rawNumber(),
            buyRequest.seatNumber(),
            true,
            false,
            String.format("%03d", (Math.round((Math.random() * (999 - 1)) + 1))),
            ticketsInfo.getPrice(),
            buyRequest.tel(),
            buyRequest.email(),
            buyRequest.fullName()
        );

        ticket.setInfo(ticketsInfo);

        ticketUserRepository.saveAndFlush(ticket);

        if (buyRequest.idUser() != null) {
            Client client = clientRepository.getReferenceById(buyRequest.idUser());

            Set<Ticket> ticketSet = client.getBasket();
            ticketSet.add(ticket);
            client.setBasket(ticketSet);

            clientRepository.save(client);
        }

        try {
            mailSender.sendTicket(ticket);
        } catch (MessagingException | IOException | WriterException e) {

            throw new RuntimeException(e);
        }

        return new TicketUserResponse(
                ticket.getId(),
                ticket.getInfo().getSession().getEvent().getEventBasicInformation().getNameRus(),
                ticket.getInfo().getSession().getPlace().getName(),
                ticket.getInfo().getSession().getPlace().getAddress(),
                String.valueOf(ticket.getInfo().getSession().getStart()),
                ticket.getInfo().getSession().getRoomLayout().getName(),
                ticket.getRowNumber(),
                ticket.getSeatNumber(),
                ticket.getPrice(),
                ticket.getEmail(),
                ticket.getPhone(),
                ticket.getFullName()
        );
    }

    @Override
    public boolean getStatus(Long sessionId, String rowNumber, String seatNumber) {
        return ticketUserRepository.getFirstBySessionAndRowAndSeat(sessionId, Integer.valueOf(rowNumber), Integer.valueOf(seatNumber)) != null ? true : false;
    }

    @Override
    public TicketsOrganizationResponse[] getTicketsOrganization(Long id) {
        Organization organization = organizationService.getOrganizationById(id);
        List<TicketsOrganizationResponse> ticketsOrganizationResponseList = new ArrayList<>();
        List<List<Session>> sessions = new ArrayList<>();

        for (Place place: organization.getPlaceSet()) {
            List<TicketsSessionOrganization> ticketsSessionOrganizationList = new ArrayList<>();

            for (Event event: organization.getEventSet()) {
                List<Ticket> tickets = ticketUserRepository.getAllTicketByEventAndPlace(place, event);
                List<TicketOrganization> ticketOrganizationList = new ArrayList<>();

                for (Ticket ticket: tickets) {
                    ticketOrganizationList.add(
                            new TicketOrganization(
                                    ticket.getId(),
                                    ticket.getFullName(),
                                    String.valueOf(ticket.getInfo().getSession().getStart()),
                                    ticket.getRowNumber(),
                                    ticket.getSeatNumber(),
                                    ticket.getIsExtinguished()
                            )
                    );
                }

                ticketsSessionOrganizationList.add(
                        new TicketsSessionOrganization(
                                event.getEventBasicInformation().getNameRus(),
                                ticketOrganizationList.toArray(TicketOrganization[]::new)
                        )
                );
            }

            ticketsOrganizationResponseList.add(
                    new TicketsOrganizationResponse(
                             place.getCity().getNameRus() + ", " + place.getAddress(),
                                     place.getName(),
                            ticketsSessionOrganizationList.toArray(TicketsSessionOrganization[]::new)
                    )
            );
        }


        return ticketsOrganizationResponseList.toArray(TicketsOrganizationResponse[]::new);
    }

    @Transactional
    @Override
    public TicketOrganization putTicketRepayment(Long id) {
        Ticket ticket = ticketUserRepository.getReferenceById(id);

        if (ticket.getIsExtinguished()) {
            List<ErrorMessage> errorMessages = new ArrayList<>();
            errorMessages.add(new ErrorMessage("The ticket redeemed", "The ticket has already been redeemed!"));
            throw new InvalidDataException(errorMessages);
        }

        ticketUserRepository.editExtinguishedByTicketById(id);

        return new TicketOrganization(
                ticket.getId(),
                ticket.getFullName(),
                String.valueOf(ticket.getInfo().getSession().getStart()),
                ticket.getRowNumber(),
                ticket.getSeatNumber(),
                true
        );
    }

    @Override
    public TicketsInfoResponse getInfoTicketsInfo(Long id) {
        TicketsInfo ticketsInfo = ticketRepository.findFirstBySessionId(id);
        return new TicketsInfoResponse(
                ticketsInfo.getId(),
                ticketsInfo.getSession().getEvent().getEventBasicInformation().getNameRus(),
                ticketsInfo.getSession().getEvent().getEventBasicInformation().getSymbolicName(),
                ticketsInfo.getSession().getTypeOfMovie(),
                ticketsInfo.getSession().getStart().toLocalDateTime(),
                ticketsInfo.getSession().getRoomLayout().getHallNumber(),
                ticketsInfo.getPrice()
        );
    }

    @Override
    public TicketsInfoBuyResponse getTicketsInfoBuy(TicketsInfoBuy ticketsInfoBuy) {
        Ticket ticket = ticketUserRepository.getFirstBySessionAndRowAndSeat(ticketsInfoBuy.id(), ticketsInfoBuy.row(), ticketsInfoBuy.seat());

        if (ticket != null) {
            List<ErrorMessage> errorMessages = new ArrayList<>();
            errorMessages.add(new ErrorMessage("The place is already occupied", "The place is already occupied!"));
            throw new InvalidDataException(errorMessages);
        }

        TicketsInfo ticketsInfo = ticketRepository.getTicketsBySession(ticketsInfoBuy.id());
        String seatGroup = hallSchemeService.getPriceByRowAndSeat(ticketsInfo.getSession().getRoomLayout(), ticketsInfoBuy.row(), ticketsInfoBuy.seat());

        if (seatGroup == null) {
            List<ErrorMessage> errorMessages = new ArrayList<>();
            errorMessages.add(new ErrorMessage("There is no place", "There is no place in the hall!"));
            throw new InvalidDataException(errorMessages);
        }

        Double price = Double.valueOf(ticketsInfo.getPrice());

        for (String group: ticketsInfo.getSession().getRoomLayout().getSeatGroupInfo()) {
            String[] groupArray = group.split(" - ");

            if (Arrays.asList(groupArray).contains(seatGroup)) {
                price *= (Double.valueOf(groupArray[1])+0.22);
            }
        }

        return new TicketsInfoBuyResponse(
                ticketsInfo.getSession().getId(),
                ticketsInfo.getSession().getPlace().getName(),
                ticketsInfo.getSession().getPlace().getCity().getNameRus() + ", " + ticketsInfo.getSession().getPlace().getAddress(),
                ticketsInfo.getSession().getRoomLayout().getHallNumber(),
                ticketsInfoBuy.row(),
                ticketsInfoBuy.seat(),
                String.format("%.2f",price),
                ticketsInfo.getSession().getStart().toLocalDateTime()
        );
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
