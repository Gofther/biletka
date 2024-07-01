package biletka.main.controller;

import biletka.main.entity.Cheque;
import biletka.main.entity.Session;
import biletka.main.entity.Ticket;
import biletka.main.repository.ChequeRepository;
import biletka.main.repository.SessionRepository;
import biletka.main.repository.TicketRepository;
import biletka.main.service.MailSender;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.MissingFormatArgumentException;
import java.util.Objects;

@Slf4j
@RestController
@RequiredArgsConstructor
public class SchedulingController {
    private final TicketRepository ticketRepository;
    private final SessionRepository sessionRepository;
    private final MailSender mailSender;
    private final ChequeRepository chequeRepository;
    @Operation(
            summary = "Проверка статуса билета",
            description = "Проверяет статус билета каждую минуту"
    )
    @Scheduled(fixedRate = 60000)
    @Async
    public void checkStatusTicket() {
        List<Ticket> tickets = ticketRepository.findAll();
        for (Ticket ticket : tickets) {
            Cheque cheque = ticket.getCheque();
            if (cheque != null && cheque.getStatus() == Cheque.Status.BUY && !cheque.isMail()) {
                ticket.setIsBought(true);
                ticketRepository.save(ticket);
                Session session = ticket.getSession();
                session.setSales(session.getSales() + 1 );
                sessionRepository.save(session);
                try {
                    log.trace("SchedulingController.checkStatusTicket / - Sending ticket with id {}", ticket.getId());
                    mailSender.sendTicket(ticket);
                } catch (Exception e) {
                    log.error("SchedulingController.checkStatusTicket / - Failed to send ticket with id {}: {}", ticket.getId(), e.getMessage());
                }
            }
        }
    }
}
