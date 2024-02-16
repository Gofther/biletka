package ru.khokhlov.biletka.controller;

import com.google.zxing.WriterException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.khokhlov.biletka.dto.request.BuyRequest;
import ru.khokhlov.biletka.dto.request.TicketEditInfo;
import ru.khokhlov.biletka.dto.request.TicketInfo;
import ru.khokhlov.biletka.dto.response.TicketUserRequest;
import ru.khokhlov.biletka.dto.response.TicketsMassiveResponse;
import ru.khokhlov.biletka.dto.response.TicketsResponse;
import ru.khokhlov.biletka.service.TicketService;
import ru.khokhlov.biletka.utils.QRGenerator;

import java.io.IOException;

@Validated
@RestController
@RequestMapping("/ticket")
@RequiredArgsConstructor
@Tag(name = "Контроллер событий", description = "Выдача событий в том или ином виде")
@Slf4j
public class TicketController {
    private final QRGenerator generator;
    private final TicketService ticketService;

    @Operation(
            summary = "Создание билетов сессии",
            description = "Позволяет заполнить и сохранить новые билеты сессии"
    )
    @PostMapping
    public ResponseEntity<TicketsResponse> createTicket(@Parameter(description = "Информация о билетах") @Valid @RequestBody TicketInfo ticketInfo) {
        log.trace("TicketController.createTicket /ticket - ticketInfo {}", ticketInfo);
        return ResponseEntity.status(HttpStatus.CREATED).body(ticketService.createTicket(ticketInfo));
    }

    @Operation(
            summary = "Обновление билетов сессии",
            description = "Позволяет изменить и сохранить измененные билеты сессии"
    )
    @PutMapping
    public ResponseEntity<TicketsResponse> editTicket(@Parameter(description = "Обновлённая информация о билетах") @Valid @RequestBody TicketEditInfo ticketEditInfo) {
        log.trace("TicketController.editTicket /ticket - ticketEditInfo {}", ticketEditInfo);
        return ResponseEntity.status(HttpStatus.OK).body(ticketService.editTicket(ticketEditInfo));
    }

    @Operation(
            summary = "Вывод всех возможных билетов",
            description = "Позволяет получить все возможные билеты"
    )
    @GetMapping
    public ResponseEntity<TicketsMassiveResponse> getAllTickets() {
        log.trace("TicketController.getAllTickets /ticket");
        return ResponseEntity.status(HttpStatus.OK).body(ticketService.getAllTickets());
    }

    //TODO переделать в нормальный метод генерации qr
    @Operation(
            summary = "Вывод всех возможных билетов",
            description = "Позволяет получить все возможные билеты"
    )
    @PostMapping(value = "/generate")
    public ResponseEntity<byte[]> generateQR() throws IOException, WriterException {
        log.trace("TicketController.getAllTickets /ticket");
        generator.generateQRCodeImage("http://ticket-zone.ru", "./src/main/resources/static/img/QRCode.png");
        return ResponseEntity.status(HttpStatus.OK).body(generator.getQRCodeImage("https://ya.ru"));
    }

    @Operation(
            summary = "Покупка билета",
            description = "Позволяет купить билет на сеанс"
    )
    @PostMapping(value = "/buy")
    public ResponseEntity<TicketUserRequest> buyTicket(@Parameter(description = "Информация для покупки") @Valid @RequestBody BuyRequest buyRequest) {
        log.trace("TicketController.getAllTickets /ticket/buy - buyRequest {} ", buyRequest);
        TicketUserRequest ticket = ticketService.postBuyTicket(buyRequest);
        return ResponseEntity.status(HttpStatus.OK).body(ticket);
    }
}
