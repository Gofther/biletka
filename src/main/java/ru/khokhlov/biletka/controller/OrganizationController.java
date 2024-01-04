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
import ru.khokhlov.biletka.dto.request.OrganizationAddEvent;
import ru.khokhlov.biletka.dto.request.OrganizationAddPlace;
import ru.khokhlov.biletka.dto.response.DeleteEventOrganization;
import ru.khokhlov.biletka.dto.response.DeleteSession;
import ru.khokhlov.biletka.dto.response.OrganizationResponse;
import ru.khokhlov.biletka.dto.response.SessionInfo;
import ru.khokhlov.biletka.dto.universal.MassivePublicEvents;
import ru.khokhlov.biletka.dto.universal.MassivePublicSessions;
import ru.khokhlov.biletka.service.OrganizationService;

@CrossOrigin
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/organization")
@Tag(name = "Контроллер организации", description = "Все для организаций")
@Slf4j
public class OrganizationController {
    public final OrganizationService organizationService;

    @PutMapping("/place")
    @Operation(
            summary = "Добавление площадки к организации",
            description = "Добавление площадки к организации и занесение в базу данных")
    public ResponseEntity<OrganizationResponse> addPlaceByOrganization(@Parameter(description = "Массив названий площадок") @Valid @RequestBody OrganizationAddPlace organizationAddPlace) {
        log.trace("OrganizationController.addPlaceByOrganization /organization/place - organizationAddPlace {}", organizationAddPlace);
        return ResponseEntity.status(HttpStatus.OK).body(organizationService.addPlaceInOrganization(organizationAddPlace));
    }

    @PutMapping("/event")
    @Operation(
            summary = "Добавление мероприятия к организации",
            description = "Добавление мероприятия к организации и занесение в базу данных")
    public ResponseEntity<OrganizationResponse> addEventByOrganization(@Parameter(description = "Массив id мероприятий") @Valid @RequestBody OrganizationAddEvent organizationAddEvent) {
        log.trace("OrganizationController.addEventByOrganization /organization/event - organizationAddEvent {}", organizationAddEvent);
        return ResponseEntity.status(HttpStatus.OK).body(organizationService.addEventInOrganization(organizationAddEvent));
    }

    @PutMapping("/{organisationId}/session/{sessionId}/change-session-status")
    @Operation(
            summary = "Изменение статуса продажи сессии",
            description = "Изменение статуса продажи сессии у организации"
    )
    public ResponseEntity<SessionInfo> editSellSession(@Parameter(description = "Информация о организации") @PathVariable Long organisationId,
                                                       @Parameter(description = "Информация о сессии") @PathVariable Long sessionId,
                                                       @Parameter(description = "Статус билетов сессии") @RequestParam Boolean status) {
        log.trace("OrganizationController.stopSellEvent /organization/{organisationId}/session/{sessionId}?status={status} - organisationId {}, sessionId {}, status {}", organisationId, sessionId, status);
        return ResponseEntity.status(HttpStatus.OK).body(organizationService.editSellSession(organisationId, sessionId, status));
    }

    @GetMapping("/sessions/{organisationId}")
    @Operation(
            summary = "Получение сессий у организации",
            description = "Получение сессий ивентов по id организации")
    public ResponseEntity<MassivePublicSessions> getAllSession(@Parameter(description = "Информация о организации") @PathVariable Long organisationId) {
        log.trace("OrganizationController.getAllSession /organization/sessions - organisationId {}", organisationId);
        return ResponseEntity.status(HttpStatus.OK).body(organizationService.getAllSession(organisationId));
    }

    @GetMapping("/events/{organisationId}")
    @Operation(
            summary = "Получение ивентов у организации",
            description = "Получение ивентов по id организации")
    public ResponseEntity<MassivePublicEvents> getAllEvents(@Parameter(description = "Информация о организации") @PathVariable Long organisationId) {
        log.trace("OrganizationController.getAllEvents /organization/events - organisationId {}", organisationId);
        return ResponseEntity.status(HttpStatus.OK).body(organizationService.getAllEvent(organisationId));
    }

    @GetMapping("/{organisationId}/session/{sessionId}")
    @Operation(
            summary = "Получение информации сессии у организации",
            description = "Получение информации сессии по id организации и id сессии"
    )
    public ResponseEntity<SessionInfo> getSession(@Parameter(description = "Информация о организации") @PathVariable Long organisationId,
                                                       @Parameter(description = "Информация о сессии") @PathVariable Long sessionId) {
        log.trace("OrganizationController.getSession /organization/{organisationId}/session/{sessionId} - organisationId {}, eventId {}", organisationId, sessionId);
        return ResponseEntity.status(HttpStatus.OK).body(organizationService.getSession(organisationId, sessionId));
    }

    @DeleteMapping("/{organisationId}/event/{eventId}")
    @Operation(
            summary = "Удаление ивента у организации",
            description = "Удаление ивента по id организации и id мероприятия"
    )
    public ResponseEntity<DeleteEventOrganization> deleteEventOrganizationRelation(@Parameter(description = "Информация о организации") @PathVariable Long organisationId,
                                                                                   @Parameter(description = "Информация о ивента") @PathVariable Long eventId) {
        log.trace("OrganizationController.deleteEvent /organization/{organisationId}/eventSymbolicName/{eventId} - organisationId {}, eventId {}", organisationId, eventId);
        return ResponseEntity.status(HttpStatus.OK).body(organizationService.deleteEventOrganizationRelation(organisationId, eventId));
    }

    @DeleteMapping("/{organisationId}/session/{sessionId}")
    @Operation(
            summary = "Удаление сессии у организации",
            description = "Удаление сессии по id организации и id сессии"
    )
    public ResponseEntity<DeleteSession> deleteSession(@Parameter(description = "Информация о организации") @PathVariable Long organisationId,
                                                       @Parameter(description = "Информация о ивента") @PathVariable Long sessionId) {
        log.trace("OrganizationController.deleteSession /organization/{organisationId}/session/{sessionId} - organisationId {}, sessionId {}", organisationId, sessionId);
        return ResponseEntity.status(HttpStatus.OK).body(organizationService.deleteSession(organisationId, sessionId));
    }
}
