package biletka.main.controller;

import biletka.main.Utils.ConvertUtils;
import biletka.main.dto.request.HallCreateRequest;
import biletka.main.dto.request.PlaceCreateRequest;
import biletka.main.dto.response.MassiveCityResponse;
import biletka.main.dto.response.MessageCreateResponse;
import biletka.main.service.CityService;
import biletka.main.service.HallService;
import biletka.main.service.PlaceService;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequestMapping("/place")
@RequiredArgsConstructor
@Tag(name = "Контроллер площадок", description = "Всё, что связано с площадками")
public class PlaceController {
    private final PlaceService placeService;
    private final HallService hallService;
    private final CityService cityService;

    private final ConvertUtils convertToJSON;

    @Operation(
            summary = "Создание площадки",
            description = "Позволяет создать площадку организации"
    )
    @PostMapping//
    public ResponseEntity<MessageCreateResponse> postPlaceCreate(@Parameter(description = "токен пользователя") @RequestHeader("Authorization") String authorization,
                                                                 @Parameter(description = "данные для создания площадки") @Valid @RequestBody PlaceCreateRequest placeCreateRequest) {
        log.trace("PlaceController.postPlaceCreate / - authorization {}, placeCreateRequest {}", authorization, placeCreateRequest);
        MessageCreateResponse messageCreateResponse = placeService.postPlaceCreate(authorization, placeCreateRequest);

        return ResponseEntity.status(HttpStatus.CREATED).body(messageCreateResponse);
    }

    @Operation(
            summary = "Создание зала",
            description = "Позволяет создать зал площадки организации"
    )
    @PostMapping("/hall")//
    public ResponseEntity<MessageCreateResponse> postHallCreate(@Parameter(description = "токен пользователя") @RequestHeader("Authorization") String authorization,
                                                                @RequestPart("file") MultipartFile file,
                                                                @RequestPart("hall_create_request") String hallCreateRequest) throws JsonProcessingException, MessagingException {
        log.trace("PlaceController.postHallCreate / - authorization {}, file {}, hallCreateRequest {}", authorization, file, hallCreateRequest);
        HallCreateRequest hallCreateRequestNew = convertToJSON.convertToJSONHallCreate(hallCreateRequest);
        MessageCreateResponse messageCreateResponse = hallService.createHall(authorization, file, hallCreateRequestNew);

        return ResponseEntity.status(HttpStatus.CREATED).body(messageCreateResponse);
    }

    @Operation(
            summary = "Вывод всех городов",
            description = "Позволяет вывести все города из бд"
    )
    @GetMapping("/city")//
    public ResponseEntity<MassiveCityResponse> getAllCity() {
        log.trace("PlaceController.getAllCity /city");
        MassiveCityResponse cities = cityService.getAllCity();
        return ResponseEntity.ok(cities);
    }
}
