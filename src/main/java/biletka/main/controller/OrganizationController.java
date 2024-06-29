package biletka.main.controller;


import biletka.main.dto.response.*;
import biletka.main.dto.response.TotalSession.TotalSession;
import biletka.main.service.OrganizationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/organization")
@RequiredArgsConstructor
@Tag(name = "Контроллер организация", description = "Всё, что связано с организациями")
@CrossOrigin
public class OrganizationController {
    private final OrganizationService organizationService;
    @Operation(
            summary = "@Добавление мероприятий для организации",
            description = "Позволяет добавить мероприятие по id для организации"
    )
    @PostMapping("/events")
    public ResponseEntity<MessageCreateResponse> postEventOrganization(@Parameter(description = "токен пользователя") @RequestHeader(value = "Authorization") String authorization,
                                                                       @Parameter(description = "id мероприятия") @RequestParam Long id) {
        log.trace("OrganizationController.postEventOrganization - authorization {} , eventId {}", authorization, id);
        MessageCreateResponse messageCreateResponse = organizationService.postEventOrganization(authorization, id);
        return ResponseEntity.status(HttpStatus.CREATED).body(messageCreateResponse);
    }

    @Operation(//
            summary = "Получение организации",
            description = "Позволяет получить организацию по токену"
    )
    @GetMapping
    public ResponseEntity<OrganizationResponse> getOrganization(@Parameter(description = "токен пользователя") @RequestHeader("Authorization") String authorization) {
        System.out.println(organizationService.getOrganization(authorization));
        return ResponseEntity.status(HttpStatus.OK).body(organizationService.getOrganization(authorization));
    }

    @Operation(
            summary = "Вывод мероприятий организации",
            description = "Позволяет вывести мероприятии у организации и общее их количество"
    )
    @GetMapping("/events")//
    public ResponseEntity<EventsOrganization> getEventOrganization(@Parameter(description = "токен пользователя") @RequestHeader(value = "Authorization") String authorization) {
        log.trace("OrganizationController.getEventOrganization /event - authorization {}", authorization);
        EventsOrganization eventsOrganization = organizationService.getEventsOrganization(authorization);
        return ResponseEntity.ok(eventsOrganization);
    }

    @Operation(
            summary = "Вывод количества сеансов на день",
            description = "Вывод количества сеансов на день по площадкам"
    )
    @GetMapping("/session_sum")
    public ResponseEntity<TotalSession> getTotalSessionByOrganization(@Parameter(description = "токен пользователя") @RequestHeader("Authorization") String authorization) {
        log.trace("OrganizationController.getTotalSessionByOrganization / - authorization {}", authorization);
        return ResponseEntity.status(HttpStatus.OK).body(organizationService.getSessionsByOrganization(authorization));
    }

    @Operation(
            summary = "Вывод площадок организации",
            description = "Позволяет вывести площадки у организации и количество залов"
    )
    @GetMapping("/places")//
    public ResponseEntity<PlacesOrganization> getPlaceOrganization(@Parameter(description = "токен пользователя") @RequestHeader(value = "Authorization") String authorization) {
        log.trace("OrganizationController.getPlaceOrganization /place - authorization {}", authorization);
        PlacesOrganization placesOrganization = organizationService.getPlacesOrganization(authorization);
        return ResponseEntity.ok(placesOrganization);
    }

    @Operation(
            summary = "Вывод залов по площадкам организации",
            description = "Позволяет вывести залы по площадкам организации"
    )
    @GetMapping("/halls")//
    public ResponseEntity<MassivePlacesAndHalls> getHallOrganization(@Parameter(description = "токен пользователя") @RequestHeader(value = "Authorization") String authorization) {
        log.trace("OrganizationController.getHallOrganization /place - authorization {}", authorization);
        MassivePlacesAndHalls massivePlacesAndHalls = organizationService.getPlacesAndSession(authorization);
        return ResponseEntity.ok(massivePlacesAndHalls);
    }

    @Operation(
            summary = "Вывод статистики продаж за месяц",
            description = "Позволяет вывести общее количество билетов, количество и процент проданных билетов, количество и процент возвратов с текущей даты за месяц"
    )
    @GetMapping("/sales/month")
    public ResponseEntity<SalesResponse> getMonthlySalesOrganization(@Parameter(description = "токен пользователя") @RequestHeader(value = "Authorization") String authorization) {
        log.trace("OrganizationController.getMonthlySalesOrganization /place - authorization {}", authorization);
        SalesResponse salesResponse = organizationService.getMonthlySalesOrganization(authorization);
        return ResponseEntity.ok(salesResponse);
    }

    @Operation(
            summary = "Вывод статистики продаж за год",
            description = "Позволяет вывести общее количество билетов, количество и процент проданных билетов c текущей даты за год по месяцам"
    )
    @GetMapping("/sales/year")
    public ResponseEntity<YearlySalesResponse> getYearlySalesOrganization(@Parameter(description = "токен пользователя") @RequestHeader(value = "Authorization") String authorization) {
        log.trace("OrganizationController.getYearlySalesOrganization /place - authorization {}", authorization);
        YearlySalesResponse yearlySalesResponse = organizationService.getYearlySalesOrganization(authorization);
        return ResponseEntity.ok(yearlySalesResponse);
    }
}
