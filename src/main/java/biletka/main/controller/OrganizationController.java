package biletka.main.controller;

import biletka.main.dto.response.EventResponse;
import biletka.main.dto.response.MassiveTotalSessions;
import biletka.main.dto.response.OrganizationResponse;
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
public class OrganizationController {
    private final OrganizationService organizationService;

    @Operation(
            summary = "Получение организации",
            description = "Позволяет получить организацию по токену"
    )
    @GetMapping
    public ResponseEntity<OrganizationResponse> getOrganization(@Parameter(description = "токен пользователя") @RequestHeader("Authorization") String authorization){
        System.out.println(organizationService.getOrganization(authorization));
        return ResponseEntity.status(HttpStatus.OK).body(organizationService.getOrganization(authorization));
    }

    @Operation(
            summary = "Вывод количества сеансов на день",
            description = "Вывод количества сеансов на день по площадками"
    )
    @GetMapping("/session_sum")
    public ResponseEntity<MassiveTotalSessions> getTotalSessionByOrganization(@Parameter(description = "токен пользователя") @RequestHeader("Authorization") String authorization){
        log.trace("OrganizationController.getTotalSessionByOrganization / - authorization {}", authorization);
        return ResponseEntity.status(HttpStatus.OK).body(organizationService.getSessionsByOrganization(authorization));
    }
}
