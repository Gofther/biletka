package ru.khokhlov.biletka.controller;

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
import ru.khokhlov.biletka.dto.request.SessionInfo;
import ru.khokhlov.biletka.dto.response.SessionResponse;
import ru.khokhlov.biletka.dto.response.SessionWidgetResponse;
import ru.khokhlov.biletka.dto.universal.PublicSession;
import ru.khokhlov.biletka.service.SessionService;

import java.time.LocalDate;

@Validated
@RestController
@RequestMapping("/{city}/event/session")
@RequiredArgsConstructor
@Tag(name = "Контроллер сессий ивента", description = "Всё, что связанно с сессиями ивента")
@Slf4j
public class SessionController {
    private final SessionService sessionService;

    @Operation(
            summary = "Создание сессии",
            description = "Создание сессии на основе мероприятия")
    @PostMapping(path = "/create")
    public ResponseEntity<SessionResponse> createSession(@Parameter(description = "Город в котором происходит сессия ивента") @PathVariable String city,
                                                         @Parameter(description = "Информация о сессии") @Valid @RequestBody SessionInfo sessionInfo) {
        log.trace("SessionController.createSession /{city}/eventSymbolicName/session - city {}, sessionInfo {}", city, sessionInfo);
        SessionResponse session = sessionService.createSession(city, sessionInfo);
        return ResponseEntity.status(HttpStatus.CREATED).body(session);
    }

    @GetMapping
    @Operation(
            summary = "Сессии по дате и месту проведения",
            description = "Получение массива сессий по фильтру(дате)")
    public ResponseEntity<PublicSession[]> getMassiveSession(@Parameter(description = "Город в котором происходит сессия ивента") @PathVariable String city,
                                                             @Parameter(description = "Дата") @RequestParam LocalDate date,
                                                             @Parameter(description = "Дата") @RequestParam String place) {
        log.trace("SessionController.getMassiveSession /{city}/eventSymbolicName/session - city {}, Date {}, Place {}", city, date, place);
        return ResponseEntity.status(HttpStatus.OK).body(sessionService.getMassiveBySessionFilter(date, place, city));
    }

    @GetMapping("/widget/{organizationId}")
    @Operation(
            summary = "Сессии по виджету",
            description = "Получение массива сессий по виджету"
    )
    public ResponseEntity<SessionWidgetResponse[]> getSessionListWidget(@Parameter(description = "Город в котором происходит сессия ивента") @PathVariable String city,
                                                                        @Parameter(description = "Виджет для поиска") @PathVariable Long organizationId) {
        log.trace("SessionController.getMassiveSession /{city}/eventSymbolicName/session/widget - city {}, eventsWidgetRequest {}", city, organizationId);
        return ResponseEntity.status(HttpStatus.OK).body(sessionService.getMassiveByWidget(city, organizationId));
    }
}
