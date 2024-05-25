package biletka.main.controller;

import biletka.main.Utils.IpAddressUtils;
import biletka.main.dto.request.AuthForm;
import biletka.main.dto.response.AuthResponse;
import biletka.main.service.AdministratorService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

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
}
