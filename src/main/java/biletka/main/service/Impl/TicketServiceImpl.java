package biletka.main.service.Impl;

import biletka.main.Utils.ActivationCode;
import biletka.main.Utils.ConvertUtils;
import biletka.main.Utils.JwtTokenUtils;
import biletka.main.dto.request.BuyTicketRequest;
import biletka.main.dto.response.BuyTicketResponse;
import biletka.main.dto.response.HallScheme.SchemeFloor;
import biletka.main.dto.response.HallScheme.SchemeRow;
import biletka.main.dto.response.HallScheme.SchemeSeat;
import biletka.main.dto.response.HallSchemeResponse;
import biletka.main.dto.response.MessageCreateResponse;
import biletka.main.entity.*;
import biletka.main.repository.ChequeRepository;
import biletka.main.repository.TicketRepository;
import biletka.main.service.SessionService;
import biletka.main.service.TicketService;
import biletka.main.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

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
    /**
     * Покупка билета
     * @param authorization токен авторизации пользователя
     * @param buyTicketRequest информация о билете
     * @return сообщение о покупке билета
     */
    @Override
    public BuyTicketResponse buyTicket(String authorization, BuyTicketRequest buyTicketRequest){
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
                null
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
}
