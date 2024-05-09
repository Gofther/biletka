package biletka.main.controller;

import biletka.main.dto.response.FavoriteResponse;
import biletka.main.service.ClientService;
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
@RequestMapping("/client")
@RequiredArgsConstructor
@Tag(name = "Контроллер клиентов", description = "Всё, что связано с клиентами")
public class ClientController {
    private final ClientService clientService;

    @Operation(
            summary = "Изменение избранного для пользователя",
            description = "Позволяет изменить мероприятие в избранном пользователю"
    )
    @PutMapping("/favorite")
    public ResponseEntity<?> putEventInClient(@Parameter(description = "токен пользователя") @RequestHeader("Authorization") String authorization,
                                              @Parameter(description = "id мероприятия") @RequestParam Long id) {
        log.trace("ClientController.putEventInClient - authorization {}, id {}", authorization, id);
        FavoriteResponse favoriteResponse = clientService.toggleEventFavorite(authorization, id);

        return ResponseEntity.accepted().body(favoriteResponse);
    }
}
