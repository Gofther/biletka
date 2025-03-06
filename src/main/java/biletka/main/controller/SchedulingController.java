package biletka.main.controller;

import biletka.main.entity.*;
import biletka.main.repository.ChequeRepository;
import biletka.main.repository.OrganizationRepository;
import biletka.main.repository.SessionRepository;
import biletka.main.repository.TicketRepository;
import biletka.main.service.MailSender;
import biletka.main.service.SchedulingService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.*;

@Slf4j
@RestController
@RequestMapping("/test")
@RequiredArgsConstructor
public class SchedulingController {
    private final TicketRepository ticketRepository;
    private final SessionRepository sessionRepository;
    private final MailSender mailSender;
    private final OrganizationRepository organizationRepository;
    private final ChequeRepository chequeRepository;
    private final SchedulingService schedulingService;
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

    @Operation(
            summary = "Отправка информации о билетах",
            description = "Отправка информации о билетах на почту организации"
    )
    @Scheduled(/*fixedRateString = "PT24H",*/ cron = "0 0-59 23 * * *")
    @Async
    @GetMapping
    public void SendTicketsInfo() {
        List<Organization> organizations = organizationRepository.findAll();
        for (Organization organization : organizations) {
            String message = "";
            String file = schedulingService.getFileWithTicketsInfo(organization);
            try {
                mailSender.sendFile(file, organization.getEmail(), message);
                log.trace("SchedulingController.SendTicketsInfo / - Sending file with tickets info {}", file);
            } catch (MessagingException | FileNotFoundException e) {
                log.error("SchedulingController.SendTicketsInfo / - Failed to send file {}: {}", file, e.getMessage());
            }

        }
    }
}
