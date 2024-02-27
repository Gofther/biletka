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
import ru.khokhlov.biletka.dto.request.*;
import ru.khokhlov.biletka.dto.request.TicketsInfoBuy;
import ru.khokhlov.biletka.dto.response.*;
import ru.khokhlov.biletka.dto.response.ticketsOrganization_full.TicketOrganization;
import ru.khokhlov.biletka.service.TicketService;
import ru.khokhlov.biletka.utils.QRGenerator;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

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
            summary = "Вывод билетов пользователя",
            description = "Позволяет получить билеты пользователя по userId"
    )
    @GetMapping("/getByUser")
    public ResponseEntity<List<TicketUserResponse>> getTicketsByUser(@Parameter(description = "userId") @Valid @RequestParam Long id) {
        log.trace("TicketController.getTicketsByUser /ticket/getByUser, userId - {}",id);
        return ResponseEntity.status(HttpStatus.OK).body(ticketService.getTicketsByUser(id));
    }


    @Operation(
            summary = "Покупка билета",
            description = "Позволяет купить билет на сеанс"
    )
    @PostMapping(value = "/buy")
    public ResponseEntity<TicketUserResponse> buyTicket(@Parameter(description = "Информация для покупки") @Valid @RequestBody BuyRequest buyRequest) {
        log.trace("TicketController.getAllTickets /ticket/buy - buyRequest {} ", buyRequest);
        TicketUserResponse ticket = ticketService.postBuyTicket(buyRequest);
        return ResponseEntity.status(HttpStatus.OK).body(ticket);
    }

    @Operation(
            summary = "Вывод билетов для организации",
            description = "Позволяет получить массив билетов для организации"
    )
    @GetMapping(value = "/organization")
    public ResponseEntity<TicketsOrganizationResponse[]> getTicketsOrganization(@Parameter(description = "id организации") @Valid @RequestParam Long id) {
        log.trace("TicketController.getTicketsOrganization /ticket/organization - id {} ", id);
        TicketsOrganizationResponse[] ticketsOrganization = ticketService.getTicketsOrganization(id);
        return ResponseEntity.status(HttpStatus.OK).body(ticketsOrganization);
    }

    @Operation(
            summary = "Погашение билета",
            description = "Позволяет погасить билет"
    )
    @PutMapping(value = "/repayment")
    public ResponseEntity<TicketOrganization> putTicketRepayment(@Parameter(description = "id билета") @Valid @RequestParam Long id) {
        log.trace("TicketController.putTicketRepayment /ticket/repayment - id {} ", id);
        TicketOrganization ticketOrganization = ticketService.putTicketRepayment(id);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(ticketOrganization);
    }

    @Operation(
            summary = "Вывод информации о билетах",
            description = "Позволяет получить инфомармацию о билетах"
    )
    @GetMapping(value = "/{id}")
    public ResponseEntity<TicketsInfoResponse> getTicketsInfo(@Parameter(description = "id билетов информации") @Valid @RequestParam Long id) {
        log.trace("TicketController.getTicketsInfo /ticket/id - id {} ", id);
        TicketsInfoResponse ticketsInfoResponse = ticketService.getInfoTicketsInfo(id);
        return ResponseEntity.status(HttpStatus.OK).body(ticketsInfoResponse);
    }

    @Operation(
            summary = "Вывод информации о билетах для покупки",
            description = "Позволяет получить инфомармацию о билетах для покупки"
    )
    @GetMapping(value = "/buy")
    public ResponseEntity<TicketsInfoBuyResponse> getTicketsInfoBuy(@Parameter(description = "информация для вывода") @Valid @RequestBody TicketsInfoBuy ticketsInfoBuy) {
        log.trace("TicketController.getTicketsInfoBuy /ticket/buy - TicketsInfoBuyResponse {} ", ticketsInfoBuy);
        TicketsInfoBuyResponse ticketsInfoBuyResponse = ticketService.getTicketsInfoBuy(ticketsInfoBuy);
        return ResponseEntity.status(HttpStatus.OK).body(ticketsInfoBuyResponse);
    }
}
