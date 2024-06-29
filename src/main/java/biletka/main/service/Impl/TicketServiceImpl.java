package biletka.main.service.Impl;

import biletka.main.Utils.ActivationCode;
import biletka.main.Utils.ConvertUtils;
import biletka.main.Utils.JwtTokenUtils;
import biletka.main.Utils.QRGenerator;
import biletka.main.controller.SchedulingController;
import biletka.main.controller.TicketController;
import biletka.main.dto.request.BuyTicketRequest;
import biletka.main.dto.response.*;
import biletka.main.dto.response.HallScheme.SchemeFloor;
import biletka.main.dto.response.HallScheme.SchemeRow;
import biletka.main.dto.response.HallScheme.SchemeSeat;
import biletka.main.entity.*;
import biletka.main.repository.ChequeRepository;
import biletka.main.repository.ClientRepository;
import biletka.main.repository.TicketRepository;
import biletka.main.service.SessionService;
import biletka.main.service.TicketService;
import biletka.main.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.zxing.WriterException;
import jakarta.mail.MessagingException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.Calendar;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class TicketServiceImpl implements TicketService {
    private final UserService userService;
    private final SessionService sessionService;
    private final JwtTokenUtils jwtTokenUtils;
    private final ConvertUtils convertUtils;
    private final TicketRepository ticketRepository;
    private final ChequeRepository chequeRepository;
    private final ActivationCode activationCode;
    private final ClientRepository clientRepository;
    @Lazy
    private final QRGenerator generator;
    /**
     * Покупка билета
     * @param authorization токен авторизации пользователя
     * @param buyTicketRequest информация о билете
     * @return сообщение о покупке билета
     */
    @Override
    public BuyTicketResponse buyTicket(String authorization, BuyTicketRequest buyTicketRequest) throws MessagingException {
        log.trace("TicketServiceImpl.buyTicket - authorization {}, buyTicketRequest {}", authorization, buyTicketRequest);
        if (authorization != null && !authorization.isEmpty()) {
            String userEmail = jwtTokenUtils.getUsernameFromToken(
                    authorization.substring(7)
            );
            Users user = userService.getUserByEmail(userEmail);

            if (user == null) {
                throw new EntityNotFoundException("A broken token!");
            }
        }
        Session session = sessionService.getSessionById(buyTicketRequest.sessionId());
        Hall hall = session.getHall();
        String schemeText = hall.getScheme();
        HallSchemeResponse scheme = null;
        try {
            scheme = convertUtils.convertToJSONSchemeCreate(schemeText);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        Optional<Boolean> isOccupied = isSeatOccupied(scheme, session.getId(), buyTicketRequest.rowNumber(),buyTicketRequest.seatNumber());

        if (isOccupied.isEmpty()) {
            throw new IllegalArgumentException("seat does not exist in scheme.");
        } else if (isOccupied.get()) {
            throw new IllegalStateException("seat is already occupied.");
        }
        Cheque cheque = new Cheque(
                "url",
                null,
                false
        );
        chequeRepository.saveAndFlush(cheque);

        Ticket ticket = new Ticket(
                buyTicketRequest.rowNumber(),
                buyTicketRequest.seatNumber(),
                activationCode.generateActivationCode(),
                true,
                false,
                false,
                false,
                session.getPrice(),
                buyTicketRequest.email(),
                buyTicketRequest.fullName(),
                buyTicketRequest.phone(),
                cheque,
                session
        );
        ticketRepository.saveAndFlush(ticket);

        if (authorization != null && !authorization.isEmpty()) {
            String userEmail = jwtTokenUtils.getUsernameFromToken(
                    authorization.substring(7)
            );
            Users user = userService.getUserByEmail(userEmail);
            Client client = clientRepository.findFirstByUser(user);
            client.addTicket(ticket);
            clientRepository.save(client);
        }
        log.trace("TicketServiceImpl.buyTicket - Ticket reserved successfully for {}", buyTicketRequest.email());
        return new BuyTicketResponse(
                "Ticket reserved successfully",
                "URL"
        );
    }
    private Optional<Boolean> isSeatOccupied(HallSchemeResponse scheme, Long sessionId, int rowNumber, int seatNumber) {
        for (SchemeFloor floor : scheme.schemeFloors()) {
            for (SchemeRow row : floor.schemeRows()) {
                if (Integer.parseInt(row.rowNumber()) == rowNumber) {
                    for (SchemeSeat seat : row.schemeSeats()) {
                        if (Integer.parseInt(seat.number()) == seatNumber) {
                            boolean occupied = ticketRepository.getFirstBySessionAndRowAndSeat(sessionId,rowNumber,seatNumber) != null;
                            return Optional.of(occupied);
                        }
                    }
                }
            }
        }
        return Optional.empty();
    }

    /**
     * Получение данных о билете
     * @param ticket билет
     * @return данные о билете для отправки на почту
     */
    @Override
    public TicketResponse getTicketResponse(Ticket ticket) throws IOException, WriterException {
        log.trace("TicketServiceImpl.getTicketResponse - ticketId {}", ticket.getId());
        Session session = ticket.getSession();
        Event event = session.getEvent();
        byte[] code =  generator.getQRCodeImage(ticket.getActivationCode());
        String qrCodeBase64 = Base64.getEncoder().encodeToString(code);

        Instant startTimeInstant = Instant.ofEpochMilli(session.getStartTime().getTime());
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

        String date = dateFormatter.format(startTimeInstant.atZone(ZoneId.systemDefault()));
        String time = timeFormatter.format(startTimeInstant.atZone(ZoneId.systemDefault()));

        return new TicketResponse(
                event.getEventBasicInformation().getName_rus(),
                session.getHall().getPlace().getCity().getCityName(),
                session.getHall().getPlace().getAddress(),
                session.getHall().getPlace().getPlaceName(),
                session.getHall().getHallName(),
                ticket.getRowNumber(),
                ticket.getSeatNumber(),
                date,
                time,
                ticket.getEmail(),
                ticket.getPhone(),
                ticket.getFullName(),
                qrCodeBase64);
    }

    /**
     * Получение данных о билете для клиента
     * @param ticket билет
     * @return данные о билете для клиента
     */
    @Override
    public ClientTicketResponse getClientTicketResponse (Ticket ticket){
        log.trace("TicketServiceImpl.getClientTicketResponse - ticketId {}", ticket.getId());
        Session session = ticket.getSession();
        Event event = session.getEvent();

        Instant startTimeInstant = Instant.ofEpochMilli(session.getStartTime().getTime());
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

        String date = dateFormatter.format(startTimeInstant.atZone(ZoneId.systemDefault()));
        String time = timeFormatter.format(startTimeInstant.atZone(ZoneId.systemDefault()));

        return new ClientTicketResponse(
                event.getEventBasicInformation().getName_rus(),
                session.getHall().getPlace().getCity().getCityName(),
                session.getHall().getPlace().getAddress(),
                session.getHall().getPlace().getPlaceName(),
                session.getHall().getHallName(),
                date,
                time);
    }
}
