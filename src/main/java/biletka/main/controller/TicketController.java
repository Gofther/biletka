package biletka.main.controller;

import biletka.main.Utils.QRGenerator;
import biletka.main.dto.request.BuyTicketRequest;
import biletka.main.dto.response.BuyTicketResponse;
import biletka.main.dto.response.MessageCreateResponse;
import biletka.main.entity.Cheque;
import biletka.main.entity.Ticket;
import biletka.main.repository.TicketRepository;
import biletka.main.service.MailSender;
import biletka.main.service.TicketService;
import com.google.zxing.WriterException;
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

import java.io.IOException;
import java.util.Objects;

@Slf4j
@RestController
@RequestMapping("/ticket")
@RequiredArgsConstructor
@Tag(name = "Контроллер билетов", description = "Всё, что связано с билетами")
public class TicketController {
    private final TicketService ticketService;
    private final TicketRepository ticketRepository;
    private final QRGenerator generator;
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

    @Operation(
            summary = "Получение QR-кода для билета",
            description = "Позволяет получить QR-код для билета"
    )
    @GetMapping(value = "/{activationCode}/qr", produces = "image/png")
    public ResponseEntity<byte[]> getTicketQRCode(@PathVariable String activationCode) throws IOException, WriterException {
        log.trace("TicketController.getTicketQRCode /ticket/{}/qr", activationCode);
        byte[] qrCode = generator.getQRCodeImage(activationCode);
        return ResponseEntity.status(HttpStatus.OK)
                .header("Content-Type", "image/png")
                .body(qrCode);
    }

    @Operation(
            summary = "Активация билета",
            description = "Позволяет использовать и погасить билет"
    )
    @PutMapping("/activate")
    public ResponseEntity<MessageCreateResponse> activateTicket(
            @RequestParam Long ticketId,
            @RequestParam String activationCode) {
        log.trace("TicketController.activateTicket - ticketId {} , activationCode {}", ticketId, activationCode);
        MessageCreateResponse messageCreateResponse = ticketService.activateTicket(ticketId,activationCode);
        return ResponseEntity.status(HttpStatus.CREATED).body(messageCreateResponse);
    }
}
