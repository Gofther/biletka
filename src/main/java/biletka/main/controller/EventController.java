package biletka.main.controller;

import biletka.main.Utils.ConvertUtils;
import biletka.main.dto.request.EventCreateRequest;
import biletka.main.dto.response.MessageCreateResponse;
import biletka.main.service.EventService;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Slf4j
@RestController
@RequestMapping("/event")
@RequiredArgsConstructor
@Tag(name = "Контроллер ивентов", description = "Всё, что связано с ивентами")
public class EventController {
    private final EventService eventService;

    private final ConvertUtils convertToJSON;

    @Operation(
            summary = "Создание мероприятия",
            description = "Позволяет создать мероприятие"
    )
    @PostMapping
    public ResponseEntity<MessageCreateResponse> postEventCreate(@Parameter(description = "токен пользователя") @RequestHeader("Authorization") String authorization,
                                             @RequestPart("file") MultipartFile file,
                                             @RequestPart("event_create_request") String eventCreateRequest) throws IOException {
        log.trace("EventController.postEventCreate / - file {}, eventCreateRequest {}", file, eventCreateRequest);
        EventCreateRequest eventCreateRequestNew = convertToJSON.convertToJSONEventCreate(eventCreateRequest);
        MessageCreateResponse message = eventService.createEvent(authorization, file, eventCreateRequestNew);

        return ResponseEntity.status(HttpStatus.CREATED).body(message);
    }
}
