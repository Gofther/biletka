package biletka.main.controller;

import biletka.main.dto.request.SessionCreateRequest;
import biletka.main.dto.response.MessageCreateResponse;
import biletka.main.service.SessionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/session")
@RequiredArgsConstructor
@Tag(name = "Контроллер сеансов", description = "Всё, что связано с сеансами")
public class SessionController {
    private final SessionService sessionService;

    @Operation(//
            summary = "Создание сеанса",
            description = "Позволяет создать сеанс меоприятия"
    )
    @PostMapping
    public ResponseEntity<MessageCreateResponse> postSessionCreate(@Parameter(description = "токен пользователя") @RequestHeader("Authorization") String authorization,
                                                                   @Parameter(description = "информация о сеанса для создания") @Valid @RequestBody SessionCreateRequest sessionCreateRequest) {
        log.trace("SessionController.postSessionCreate / - authorization {}, sessionCreateRequest {}", authorization, sessionCreateRequest);
        MessageCreateResponse messageCreateResponse = sessionService.sessionCreate(authorization, sessionCreateRequest);

        return ResponseEntity.status(HttpStatus.CREATED).body(messageCreateResponse);
    }
}
