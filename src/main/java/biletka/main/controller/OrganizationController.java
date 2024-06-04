package biletka.main.controller;


import biletka.main.dto.response.EventsOrganization;
import biletka.main.dto.response.OrganizationResponse;
import biletka.main.dto.response.TotalSession.TotalSession;
import biletka.main.dto.response.MassivePlacesAndHalls;
import biletka.main.dto.response.PlacesOrganization;
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
            summary = "Получение организации",
            description = "Позволяет получить организацию по токену"
    )
    @GetMapping
    public ResponseEntity<OrganizationResponse> getOrganization(@Parameter(description = "токен пользователя") @RequestHeader("Authorization") String authorization) {
        System.out.println(organizationService.getOrganization(authorization));
        return ResponseEntity.status(HttpStatus.OK).body(organizationService.getOrganization(authorization));
    }

    @GetMapping("/events")
    public ResponseEntity<EventsOrganization> getEventOrganization(@Parameter(description = "токен пользователя") @RequestHeader(value = "Authorization") String authorization) {
        log.trace("OrganizationController.getEventOrganization /event - authorization {}", authorization);
        EventsOrganization eventsOrganization = organizationService.getEventsOrganization(authorization);
        return ResponseEntity.ok(eventsOrganization);
    }

    @Operation(
            summary = "Вывод количества сеансов на день",
            description = "Вывод количества сеансов на день по площадками"
    )
    @GetMapping("/session_sum")
    public ResponseEntity<TotalSession> getTotalSessionByOrganization(@Parameter(description = "токен пользователя") @RequestHeader("Authorization") String authorization) {
        log.trace("OrganizationController.getTotalSessionByOrganization / - authorization {}", authorization);
        return ResponseEntity.status(HttpStatus.OK).body(organizationService.getSessionsByOrganization(authorization));
    }
    @GetMapping("/places")
    public ResponseEntity<PlacesOrganization> getPlaceOrganization(@Parameter(description = "токен пользователя") @RequestHeader(value = "Authorization") String authorization) {
        log.trace("OrganizationController.getPlaceOrganization /place - authorization {}", authorization);
        PlacesOrganization placesOrganization = organizationService.getPlacesOrganization(authorization);
        return ResponseEntity.ok(placesOrganization);
    }

    @Operation(
            summary = "Вывод залов по площадкам организации",
            description = "Позволяет вывести залы по площадкам организации"
    )
    @GetMapping("/halls")
    public ResponseEntity<MassivePlacesAndHalls> getHallOrganization(@Parameter(description = "токен пользователя") @RequestHeader(value = "Authorization") String authorization) {
        log.trace("OrganizationController.getHallOrganization /place - authorization {}", authorization);
        MassivePlacesAndHalls massivePlacesAndHalls = organizationService.getPlacesAndSession(authorization);
        return ResponseEntity.ok(massivePlacesAndHalls);
    }
}
