package biletka.main.controller;

import biletka.main.Utils.ConvertUtils;
import biletka.main.dto.request.EventCreateRequest;
import biletka.main.dto.response.MassiveGenreResponse;
import biletka.main.dto.response.MessageCreateResponse;
import biletka.main.dto.response.MassiveTypeResponse;
import biletka.main.dto.universal.MassivePublicEvent;
import biletka.main.dto.universal.PublicEventImage;
import biletka.main.dto.universal.PublicFullInfoEvent;
import biletka.main.service.EventService;
import biletka.main.service.GenreService;
import biletka.main.service.TypeEventService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.http.HttpResponse;
import java.sql.Timestamp;
import java.util.Date;

@Slf4j
@RestController
@RequestMapping("/event")
@RequiredArgsConstructor
@Tag(name = "Контроллер ивентов", description = "Всё, что связано с ивентами")
@CrossOrigin
public class EventController {
    private final EventService eventService;
    private final GenreService genreService;
    private final TypeEventService typeEventService;

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
                                                            @Parameter(description = "отсчет мероприятий") @RequestParam Integer offset,
                                                            @Parameter(description = "дата для выборки") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) @RequestParam Date date) {
        log.trace("EventController.getEventLimit / - cityName {}, authorization {}, offset {}, date {}", cityName, authorization, offset, date);
        MassivePublicEvent massivePublicEvent = eventService.getEventLimit(cityName, authorization, offset, date);
        return ResponseEntity.ok(massivePublicEvent);
    }

    @Operation(
            summary = "Вывод 10 анонсов",
            description = "Вывод 10 мероприятий по городу и будущим сеансам, которых не было"
    )
    @GetMapping("/{cityName}/announcement")
    public ResponseEntity<MassivePublicEvent> getAnnouncementLimit(@Parameter(description = "название города") @PathVariable String cityName,
                                                  @Parameter(description = "токен пользователя") @RequestHeader(value = "Authorization", required = false) String authorization,
                                                  @Parameter(description = "отсчет мероприятий") @RequestParam Integer offset,
                                                  @Parameter(description = "дата для выборки") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date date) {
        log.trace("EventController.getAnnouncementLimit / - cityName {}, authorization {}, offset {}, date {}", cityName, authorization, offset, date);
        MassivePublicEvent massivePublicEvent = eventService.getAnnouncementLimit(cityName, authorization, offset, date);
        return ResponseEntity.ok(massivePublicEvent);
    }

    @Operation(
            summary = "Вывод полной информации о мероприятии",
            description = "Вывод полной информации о мероприятии по id и символьному названию"
    )
    @GetMapping("/{cityName}/{eventName}")
    public ResponseEntity<PublicFullInfoEvent> getFullInfoEvent(@Parameter(description = "название города") @PathVariable String cityName,
                                              @Parameter(description = "название мероприятия") @PathVariable String eventName,
                                              @Parameter(description = "токен пользователя") @RequestHeader(value = "Authorization", required = false) String authorization,
                                              @Parameter(description = "дата для выборки") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date date) {
        log.trace("EventController.getAnnouncementLimit / - cityName {}, eventName {}, authorization {}, date {}", cityName, eventName, authorization, date);
        PublicFullInfoEvent publicFullInfoEvent = eventService.getFullInfoEvent(authorization, cityName, eventName, date);
        return ResponseEntity.ok(publicFullInfoEvent);
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

    @Operation(
            summary = "Вывод мероприятий по возрасту",
            description = "Вывод мероприятий по указанному возрасту"
    )
    @GetMapping("/{cityName}/age")
    public ResponseEntity<MassivePublicEvent> getEventByAge(@Parameter(description = "название города") @PathVariable String cityName,
                                                            @Parameter(description = "возраст") @RequestParam int age,
                                                            @Parameter(description = "токен пользователя") @RequestHeader(value = "Authorization", required = false) String authorization,
                                                            @Parameter(description = "отсчет мероприятий") @RequestParam Integer offset,
                                                            @Parameter(description = "дата для выборки") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) @RequestParam Date date) {
        log.trace("EventController.getEventByAge / - cityName {} / - age {}, authorization {}, offset {}, date {}", cityName, age, authorization, offset, date);
        MassivePublicEvent massivePublicEvent = eventService.getEventsByCityAndAgeLimit(cityName, age, authorization, offset, date);
        return ResponseEntity.ok(massivePublicEvent);
    }

            @Operation(
            summary = "Вывод мероприятий по типу",
            description = "Вывод мероприятий по указанному типу"
    )
    @GetMapping("/{cityName}/type")
    public ResponseEntity<MassivePublicEvent> getEventByType(@Parameter(description = "название города") @PathVariable String cityName,
                                                            @Parameter(description = "тип") @RequestParam String type,
                                                            @Parameter(description = "токен пользователя") @RequestHeader(value = "Authorization", required = false) String authorization,
                                                            @Parameter(description = "отсчет мероприятий") @RequestParam Integer offset,
                                                            @Parameter(description = "дата для выборки") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) @RequestParam Date date) {
        log.trace("EventController.getEventByType / - cityName {} / - type {}, authorization {}, offset {}, date {}", cityName, type, authorization, offset, date);
        MassivePublicEvent massivePublicEvent = eventService.getEventsByCityAndType(cityName, type, authorization, offset, date);
        return ResponseEntity.ok(massivePublicEvent);
    }


    @Operation(
            summary = "Вывод мероприятий по жанру",
            description = "Вывод мероприятий по указанному женру"
    )
    @GetMapping("/{cityName}/genre")
    public ResponseEntity<MassivePublicEvent> getEventByGenre(@Parameter(description = "название города") @PathVariable String cityName,
                                                             @Parameter(description = "жанр") @RequestParam String genre,
                                                             @Parameter(description = "токен пользователя") @RequestHeader(value = "Authorization", required = false) String authorization,
                                                             @Parameter(description = "отсчет мероприятий") @RequestParam Integer offset,
                                                             @Parameter(description = "дата для выборки") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) @RequestParam Date date) {
        log.trace("EventController.getEventByGenre / - cityName {} / - genre {}, authorization {}, offset {}, date {}", cityName, genre, authorization, offset, date);
        MassivePublicEvent massivePublicEvent = eventService.getEventsByCityAndGenre(cityName, genre, authorization, offset, date);
        return ResponseEntity.ok(massivePublicEvent);
    }



    @Operation(
            summary = "Вывод жанров",
            description = "Позволяет вывести все возможные жанры"
    )
    @GetMapping("/genre")
    public ResponseEntity<MassiveGenreResponse> getAllGenre() {
        log.trace("EventController.getAllGenre  /genre");
        MassiveGenreResponse genres = genreService.getAllGenre();
        return ResponseEntity.ok(genres);
    }

    @Operation(
            summary = "Вывод типов мероприятия",
            description = "Позволяет вывести все возможные типы мероприятий"
    )
    @GetMapping("/type")
    public ResponseEntity<MassiveTypeResponse> getAllType() {
        log.trace("EventController.getAllType  /type");
        MassiveTypeResponse types = typeEventService.getAllType();
        return ResponseEntity.ok(types);
    }
}
