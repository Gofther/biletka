package biletka.main.controller;

import biletka.main.dto.request.AuthForm;
import biletka.main.dto.response.AuthResponse;
import biletka.main.service.UserService;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/security")
@RequiredArgsConstructor
@Tag(name = "Контроллер безопасности", description = "Всё, что связано с безопасностью")
public class SecurityController {
    private final UserService userService;

    @Operation(
            summary = "Аутентификация пользователя",
            description = "Ползволяется пройти аутентификацию и получить jwt"
    )
    @Hidden
    @PostMapping("/auth")
    public ResponseEntity<?> createAuthToken(@Parameter(description = "Форма авторизации") @Valid @RequestBody AuthForm authForm) {
        log.trace("SecurityController.createAuthToken /auth - authForm {}", authForm);
        AuthResponse authResponse = userService.getAuthToken(authForm);

        return ResponseEntity.ok(authResponse);
    }
}
