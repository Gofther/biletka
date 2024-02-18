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
import ru.khokhlov.biletka.dto.request.HallCreationRequestDTO;
import ru.khokhlov.biletka.dto.request.PlaceInfo;
import ru.khokhlov.biletka.dto.response.HallCreationResponseDTO;
import ru.khokhlov.biletka.dto.response.PlaceResponse;
import ru.khokhlov.biletka.dto.response.SchemeResponse;
import ru.khokhlov.biletka.entity.HallScheme;
import ru.khokhlov.biletka.entity.Place;
import ru.khokhlov.biletka.service.HallSchemeService;
import ru.khokhlov.biletka.service.PlaceService;

import java.util.List;

@Validated
@RestController
@RequestMapping("/{city}/place")
@RequiredArgsConstructor
@Tag(name = " Контроллер мест проведения мероприятий", description = "Всё, что связанно с местами проведения мероприятий")
@Slf4j
public class PlaceController {

    private final PlaceService placeService;
    private final HallSchemeService hallSchemeService;

    @Operation(
            summary = "Создание площадки",
            description = "Занесение площадки в базу данных")
    @PostMapping
    public ResponseEntity<PlaceResponse> createPlace(@Parameter(description = "Город в котором будет площадка") @PathVariable String city,
                                                     @Parameter(description = "Информация об площадке для регистрации") @Valid @RequestBody PlaceInfo placeInfo) {
        log.trace("PlaceController.createPlace /{city}/place - city {}, placeInfo {}", city, placeInfo);

        PlaceResponse place = placeService.createPlace(city, placeInfo);

        return ResponseEntity.status(HttpStatus.CREATED).body(place);
    }

    @PostMapping(path = "/hall")
    public ResponseEntity<HallCreationResponseDTO> createHall(@Parameter(description = "Город в котором будет зал") @PathVariable String city,
                                                              @Parameter(description = "Информация об зале") @Valid @RequestBody HallCreationRequestDTO hallCreationRequestDTO) {
        log.trace("PlaceController.createHall /{city}/place/hall - city {}, HallRequestDTO {}", city, hallCreationRequestDTO);

        HallCreationResponseDTO responseDTO = hallSchemeService.createHallScheme(city, hallCreationRequestDTO);

        log.trace("PlaceController.createHall /{city}/place/hall - HallResponseDTO {}", responseDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
    }

    @PutMapping(path = "/hall")
    public ResponseEntity<String> putHall(@Parameter(description = "Схема зала") String scheme,
                                          @Parameter(description = "Схема зала") Long id) {
        log.trace("PlaceController.putHall /{city}/place/hall - hallScheme {}, id {}", scheme, id);
        String hall = hallSchemeService.putHall(scheme, id);
        return ResponseEntity.status(HttpStatus.OK).body(hall);
    }

    @GetMapping(path = "/hall/{organizationId}")
    public ResponseEntity<List<HallScheme>> getAllHallByOrganization(@Parameter(description = "Город в котором будет зал") @PathVariable String city,
                                                                     @Parameter(description = "Информация об зале") @PathVariable Long organizationId) {
        log.trace("PlaceController.createHall /{city}/place/hall - city {}, organizationId {}", city, organizationId);

        List<HallScheme> responseDTO = hallSchemeService.getAllHallByOrganization(city, organizationId);

        log.trace("PlaceController.createHall /{city}/place/hall - List<HallScheme> {}", responseDTO);
        return ResponseEntity.status(HttpStatus.OK).body(responseDTO);
    }

    @GetMapping(path = "/all-organization-place/{organizationId}")
    public ResponseEntity<List<Place>> getAllPlaceByOrganization(@Parameter(description = "Город в котором будет зал") @PathVariable String city,
                                                                 @Parameter(description = "Информация об зале") @PathVariable Long organizationId) {
        log.trace("PlaceController.createHall /{city}/place/hall - city {}, organizationId {}", city, organizationId);

        List<Place> responseDTO = placeService.getAllPlaceByOrganization(city, organizationId);

        log.trace("PlaceController.createHall /{city}/place/hall - HallResponseDTO {}", responseDTO);
        return ResponseEntity.status(HttpStatus.OK).body(responseDTO);
    }

    @GetMapping(path = "/scheme/{hallId}")
    public ResponseEntity<?> getScheme(@Parameter(description = "id зала") @PathVariable Long hallId) {
        SchemeResponse schemeResponse = hallSchemeService.getScheme(hallId);
        return ResponseEntity.status(HttpStatus.OK).body(schemeResponse);
    }

    @GetMapping(path = "/scheme/{hallId}/{sessionId}")
    public ResponseEntity<?> getSchemeBySession(@Parameter(description = "id зала") @PathVariable Long hallId,
                                       @Parameter(description = "id сессии") @PathVariable Long sessionId) {
        SchemeResponse schemeResponse = hallSchemeService.getSchemeBySession(hallId, sessionId);
        return ResponseEntity.status(HttpStatus.OK).body(schemeResponse);
    }
}



