package ru.khokhlov.biletka.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.khokhlov.biletka.dto.request.EventInfo;
import ru.khokhlov.biletka.dto.response.*;
import ru.khokhlov.biletka.dto.universal.MassivePublicEvents;
import ru.khokhlov.biletka.service.EventService;

import java.time.LocalDate;

@Validated
@RestController
@RequestMapping("/{city}/event")
@RequiredArgsConstructor
@Tag(name = "Контроллер событий", description = "Выдача событий в том или ином виде")
@Slf4j
public class EventController {
    private final EventService eventService;

    @Operation(
            summary = "Создание мероприятия",
            description = "Позволяет заполнить и сохранить новое мероприятие")
    @PostMapping
    public ResponseEntity<EventResponse> createEvent(@Parameter(description = "Информация о событии") @Valid @RequestBody EventInfo eventInfo,
                                                     @Parameter(description = "Город в котором будет событие") @PathVariable String city) {
        log.trace("EventController.createEvent /{city}/event - eventInfo {}", eventInfo);
        EventResponse event = eventService.createEvent(eventInfo);

        return ResponseEntity.status(HttpStatus.CREATED).body(event);
    }

    @Operation(
            summary = "Получение всех мероприятий",
            description = "Получение всех мероприятий"
    )
    @GetMapping
    public ResponseEntity<MassiveOfEvents> getAllEvent(@PathVariable String city) {
        log.trace("EventController.createEvent /{city}/event");
        return ResponseEntity.status(HttpStatus.OK).body(eventService.getAllEvent());
    }

    @Operation(
            summary = "Получение дат данного события в городе",
            description = "Позволяет получить массив дат мероприятия в определенном городе")
    @GetMapping(path = "/{eventSymbolicName}/{id}")
    public ResponseEntity<DatesOfEvents> getDatesOfEvent(@Parameter(description = "Город в котором будет событие") @PathVariable String city,
                                                         @Parameter(description = "Символьное имя события") @PathVariable String eventSymbolicName,
                                                         @Parameter(description = "id мероприятия") @PathVariable Long id) {
        log.trace("EventController.createEvent /{city}/eventSymbolicName/{eventSymbolicName} - Event symbolic name {}, city {}, id {}", eventSymbolicName, city, id);
        return ResponseEntity.status(HttpStatus.OK).body(eventService.getDateOfEvent(eventSymbolicName, city, id));
    }

    @GetMapping(path = "/{placeName}")
    @Operation(
            summary = "Получение ивентов на площадке",
            description = "Получение ивентов на площадке по названию города, названию площадки и дате")
    public ResponseEntity<MassivePublicEvents> getAllEventByDate(@Parameter(description = "Город в котором происходит поиск площадки") @PathVariable String city,
                                                                 @Parameter(description = "Название площадки") @PathVariable String placeName,
                                                                 @Parameter(description = "Дата для поиска ивентов(Может быть пуста)") @RequestParam LocalDate date) {
        log.trace("PlaceController.getAllEventByDate /{city}/place/{name} - city {}, name {}, date {}", city, placeName, date);
        return ResponseEntity.status(HttpStatus.OK).body(eventService.getAllEventByDate(city, placeName, date));
    }

    @Operation(
            summary = "Получение деталей события",
            description = "Позволяет получить всю информацию о событии")
    @GetMapping(path = "/{eventSymbolicName}/info/{id}")
    public ResponseEntity<EventDetail> getEventDetail(@Parameter(description = "Город в котором будет событие") @PathVariable String city,
                                                      @Parameter(description = "Символьное имя события") @PathVariable String eventSymbolicName,
                                                      @Parameter(description = "id мероприятия") @PathVariable Long id) {
        log.trace("EventController.createEvent /{city}/eventSymbolicName/{eventSymbolicName}/info - Event symbolic name {}, city {}, id {}", eventSymbolicName, city, id);
        return ResponseEntity.status(HttpStatus.OK).body(eventService.getEventDetail(eventSymbolicName, city, id));
    }

    @Operation(
            summary = "Получение ближайших событий в городе",
            description = "Позволяет получить массив мероприятий в определенном городе на неделю вперед")
    @GetMapping(path = "/nearest-events")
    public ResponseEntity<MassiveOfEvents> getNearestEvents(@Parameter(description = "Город в котором будет событие") @PathVariable String city) {
        log.trace("EventController.createEvent /{city}/eventSymbolicName/nearest-events - city {}", city);
        return ResponseEntity.status(HttpStatus.OK).body(eventService.getNearestEvents(city));
    }

    @Operation(
            summary = "Получение популярных событий",
            description = "?")
    @GetMapping(path = "/popular")
    public ResponseEntity<MassiveOfEvents> getPopularEvents(@Parameter(description = "Город в котором будет событие") @PathVariable String city) {
        log.trace("EventController.createEvent /{city}/eventSymbolicName/popular - city {}", city);
        return ResponseEntity.status(HttpStatus.OK).body(eventService.getNearestEvents(city));
    }

    @GetMapping(path = "/{eventSymbolicName}/{id}/places")
    public ResponseEntity<PlacesAndInfoSession[]> getPlacesAndInfo(@Parameter(description = "Город в котором будет событие") @PathVariable String city,
                                                                   @Parameter(description = "Название события") @PathVariable String eventSymbolicName,
                                                                   @Parameter(description = "id события") @PathVariable Long id) {
        log.trace("EventController.createEvent /{city}/{eventSymbolicName}/{id}/places - city {}, eventSymbolicName {}, id {}", city, eventSymbolicName, id);
        return ResponseEntity.ok().body(eventService.getPlacesAndInfo(city, id));
    }

    @DeleteMapping(path = "/remove")
    public ResponseEntity<EventResponse> deleteById(@Parameter(description = "Город в котором будет событие") @PathVariable String city,
                                                    @Parameter(description = "id события") @RequestParam Long id) {
        log.trace("EventController.createEvent /remove - id {}", id);
        return ResponseEntity.ok().body(eventService.removeEventById(id));
    }
}


