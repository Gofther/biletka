package biletka.main.controller;

import biletka.main.dto.request.BuyTicketRequest;
import biletka.main.dto.response.BuyTicketResponse;
import biletka.main.dto.response.MessageCreateResponse;
import biletka.main.entity.Cheque;
import biletka.main.entity.Ticket;
import biletka.main.repository.TicketRepository;
import biletka.main.service.MailSender;
import biletka.main.service.TicketService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@Slf4j
@RestController
@RequestMapping("/ticket")
@RequiredArgsConstructor
@Tag(name = "Контроллер билетов", description = "Всё, что связано с билетами")
public class TicketController {
    private final TicketService ticketService;
    private final TicketRepository ticketRepository;
    private final MailSender mailSender;
    @Operation(
            summary = "Покупка билетов",
            description = "Позволяет купить билет"
    )
    @PostMapping("/buy")
    public ResponseEntity<BuyTicketResponse> buyTicket(
            @Parameter(description = "токен пользователя", required = false) @RequestHeader(value = "Authorization",required = false) String authorization,
            @RequestBody BuyTicketRequest buyTicketRequest) throws MessagingException {
        log.trace("TicketController.buyTicket - authorization {} , buyTicketRequest {}", authorization, buyTicketRequest);
        BuyTicketResponse buyTicketResponse = ticketService.buyTicket(authorization,buyTicketRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(buyTicketResponse);
    }
}
