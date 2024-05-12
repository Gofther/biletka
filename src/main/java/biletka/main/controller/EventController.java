package biletka.main.controller;

import biletka.main.Utils.ConvertUtils;
import biletka.main.dto.request.EventCreateRequest;
import biletka.main.dto.response.MessageCreateResponse;
import biletka.main.dto.universal.MassivePublicEvent;
import biletka.main.dto.universal.PublicEventImage;
import biletka.main.service.EventService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.http.HttpResponse;

@Slf4j
@RestController
@RequestMapping("/event")
@RequiredArgsConstructor
@Tag(name = "Контроллер ивентов", description = "Всё, что связано с ивентами")
@CrossOrigin
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

    @Operation(
            summary = "Вывод 10 мероприятий",
            description = "Вывод 10 мероприятий по городу"
    )
    @GetMapping("/{cityName}")
    public ResponseEntity<MassivePublicEvent> getEventLimit(@Parameter(description = "название города") @PathVariable String cityName,
                                                            @Parameter(description = "токен пользователя") @RequestHeader(value = "Authorization", required = false) String authorization,
                                                            @Parameter(description = "отсчет мероприятий") @RequestParam Integer offset) {
        log.trace("EventController.getEventLimit / - cityName {}, authorization {}, offset {}", cityName, authorization, offset);
        MassivePublicEvent massivePublicEvent = eventService.getEventLimit(cityName, authorization, offset);
        return ResponseEntity.ok(massivePublicEvent);
    }

    @Operation(
            summary = "Вывод 10 анонсов",
            description = "Вывод 10 мероприятий по городу и будущим сеансам, которых не было"
    )
    @GetMapping("/{cityName}/announcement")
    public ResponseEntity<MassivePublicEvent> getAnnouncementLimit(@Parameter(description = "название города") @PathVariable String cityName,
                                                  @Parameter(description = "токен пользователя") @RequestHeader(value = "Authorization", required = false) String authorization,
                                                  @Parameter(description = "отсчет мероприятий") @RequestParam Integer offset) {
        log.trace("EventController.getAnnouncementLimit / - cityName {}, authorization {}, offset {}", cityName, authorization, offset);
        MassivePublicEvent massivePublicEvent = eventService.getAnnouncementLimit(cityName, authorization, offset);
        return ResponseEntity.ok(massivePublicEvent);
    }

    @CrossOrigin
    @Operation(
            summary = "Вывод изображения мероприятия",
            description = "Вывод изображения мероприятия"
    )
    @GetMapping("/img/{id}>>{symbolicName}")
    public void getImageEvent(@PathVariable String id,
                              @PathVariable String symbolicName,
                              HttpServletResponse response) throws IOException {
        log.trace("EventController.getImageEvent  /img/{id}-{symbolicName} - id {}, symbolicName {}", id, symbolicName);
        PublicEventImage publicEventImage = eventService.getImageEvent(id, symbolicName);
        response.setContentType(publicEventImage.type());
        response.getOutputStream().write(publicEventImage.imageData());
        response.getOutputStream().close();
    }
}
