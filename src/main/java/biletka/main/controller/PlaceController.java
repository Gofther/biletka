package biletka.main.controller;

import biletka.main.dto.request.PlaceCreateRequest;
import biletka.main.dto.response.MessageCreateResponse;
import biletka.main.service.PlaceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/place")
@RequiredArgsConstructor
@Tag(name = "Контроллер площадок", description = "Всё, что связано с площадками")
public class PlaceController {
    private final PlaceService placeService;

    @Operation(
            summary = "Создание площадки",
            description = "Позволяет создать площадку организации"
    )
    @PostMapping
    public ResponseEntity<MessageCreateResponse> postPlaceCreate(@Parameter(description = "токен пользователя") @RequestHeader("Authorization") String authorization,
                                                                 @Parameter(description = "данные для создания площадки") @Valid @RequestBody PlaceCreateRequest placeCreateRequest) {
        log.trace("PlaceController.postPlaceCreate / - authorization {}, placeCreateRequest {}", authorization, placeCreateRequest);
        MessageCreateResponse messageCreateResponse = placeService.postPlaceCreate(authorization, placeCreateRequest);

        return ResponseEntity.status(HttpStatus.CREATED).body(messageCreateResponse);
    }
}
