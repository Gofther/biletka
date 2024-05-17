package biletka.main.controller;

import biletka.main.dto.response.EventsOrganization;
import biletka.main.service.OrganizationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/organization")
@RequiredArgsConstructor
@Tag(name = "Контроллер ивентов", description = "Всё, что связано с ивентами")
@CrossOrigin
public class OrganizationController {
    private final OrganizationService organizationService;

    @Operation(
            summary = "Вывод мероприятий организации",
            description = "Позволяет вывести мероприятии у организации и общее их количество"
    )
    @GetMapping("/event")
    public ResponseEntity<?> getEventOrganization(@Parameter(description = "токен пользователя") @RequestHeader(value = "Authorization") String authorization) {
        log.trace("OrganizationController.getEventOrganization /event - authorization {}", authorization);
        EventsOrganization eventsOrganization = organizationService.getEventsOrganization(authorization);
        return ResponseEntity.ok(eventsOrganization);
    }
}
