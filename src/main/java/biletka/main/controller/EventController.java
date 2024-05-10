package biletka.main.controller;

import biletka.main.Utils.ConvertUtils;
import biletka.main.dto.request.EventCreateRequest;
import biletka.main.dto.response.EventResponse;
import biletka.main.dto.response.MessageCreateResponse;
import biletka.main.service.EventService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
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
    public ResponseEntity<?> postEventCreate(@Parameter(description = "токен пользователя") @RequestHeader("Authorization") String authorization,
                                             @RequestPart("file") MultipartFile file,
                                             @RequestPart("event_create_request") String eventCreateRequest) throws IOException {
        log.trace("EventController.postEventCreate / - file {}, eventCreateRequest {}", file, eventCreateRequest);
        EventCreateRequest eventCreateRequestNew = convertToJSON.convertToJSONEventCreate(eventCreateRequest);
        MessageCreateResponse message = eventService.createEvent(authorization, file, eventCreateRequestNew);

        return ResponseEntity.status(HttpStatus.CREATED).body(message);
    }

    @Operation(
            summary = "Получение мероприятия по id",
            description = "Позволяет получить мероприятие по id"
    )
    @GetMapping
    public ResponseEntity<EventResponse> getEventOfId(@Parameter(description = "id") @RequestParam Long id){
        System.out.println(eventService.getEventOfId(id));
        return ResponseEntity.status(HttpStatus.OK).body(eventService.getEventOfId(id));
    }

    @Operation(
            summary = "Получение мероприятия по id и названию",
            description = "Позволяет получить мероприятие по id и названию"
    )
    @GetMapping(path = "/{name}")
    public ResponseEntity<EventResponse> getEventOfIdAndName(@Parameter(description = "id-name") @PathVariable @Valid  String name) {
        return ResponseEntity.status(HttpStatus.OK).body(eventService.getEventOfName(name));
    }
}
