package biletka.main.controller;

import biletka.main.Utils.IpAddressUtils;
import biletka.main.dto.request.AuthForm;
import biletka.main.dto.request.PlaceCreateRequest;
import biletka.main.dto.response.AuthResponse;
import biletka.main.dto.response.MessageCreateResponse;
import biletka.main.service.AdministratorService;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Slf4j
@RestController
@RequestMapping("/dGlja2V0QWRtaW4=")
@RequiredArgsConstructor
@Tag(name = "Контроллер администрации", description = "Всё, что связано с администрированием")
public class AdministratorController {
    private final AuthenticationManager authenticationManager;
    private final AdministratorService administratorService;

    @PostMapping
    public ResponseEntity<AuthResponse> postAuth(@Parameter(description = "Форма авторизации") @Valid @RequestBody AuthForm authForm,
                                                 @Parameter(description = "Http данные") HttpServletRequest request) {
        log.trace("AdministratorController.postAuth - / - authForm {}", authForm);
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authForm.email(), authForm.password()));
        AuthResponse authResponse = administratorService.postAuth(authForm, request);
        return ResponseEntity.ok(authResponse);
    }

    @Operation(
            summary = "Схема зала",
            description = "Сохранение схемы зала"
    )
    @PostMapping("/hall")
    public ResponseEntity<MessageCreateResponse> postHallScheme(@Parameter(description = "токен пользователя") @RequestHeader("Authorization") String authorization,
                                                                @Parameter(description = "id зала")  @RequestParam Long hallId,
                                                                @Parameter(description = "svg файл зала")@RequestParam MultipartFile file,
                                                                @Parameter(description = "схема зала")@RequestParam String scheme) throws IOException {
        log.trace("AdministratorController.postHallScheme / - authorization {}, hallId {}, file {}, scheme {}", authorization, hallId, file, scheme );
        MessageCreateResponse messageCreateResponse = administratorService.postHallScheme(authorization, hallId,file,scheme);

        return ResponseEntity.status(HttpStatus.CREATED).body(messageCreateResponse);
    }
}
