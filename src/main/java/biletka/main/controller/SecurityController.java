package biletka.main.controller;

import biletka.main.Utils.IpAddressUtils;
import biletka.main.dto.request.ActiveClientRequest;
import biletka.main.dto.request.AuthForm;
import biletka.main.dto.request.ClientRegistrationRequest;
import biletka.main.dto.request.OrganizationRegistrationRequest;
import biletka.main.dto.response.AuthResponse;
import biletka.main.dto.response.ClientRegistrationResponse;
import biletka.main.dto.response.MessageCreateResponse;
import biletka.main.service.UserService;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;

@Slf4j
@RestController
@RequestMapping("/security")
@RequiredArgsConstructor
@Tag(name = "Контроллер безопасности", description = "Всё, что связано с безопасностью")
public class SecurityController {
    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final IpAddressUtils ipAddressUtils;

    @Operation(
            summary = "Аутентификация пользователя",
            description = "Ползволяется пройти аутентификацию и получить jwt"
    )
    @Hidden
    @PostMapping("/auth")//
    public ResponseEntity<AuthResponse> createAuthToken(@Parameter(description = "Форма авторизации") @Valid @RequestBody AuthForm authForm) {
        log.trace("SecurityController.createAuthToken /auth - authForm {}", authForm);
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authForm.email(), authForm.password()));
        AuthResponse authResponse = userService.getAuthToken(authForm);

        return ResponseEntity.ok(authResponse);
    }

    @Operation(
            summary = "Регистрация обычного пользователя",
            description = "Позволяет сохранить нового пользователя в базе данных"
    )
    @PostMapping//
    public ResponseEntity<ClientRegistrationResponse> postClientRegistration(@Parameter(description = "Данные для регистрации нового пользователя") @Valid @RequestBody ClientRegistrationRequest clientRegistrationRequest) throws ParseException, MessagingException {
        log.trace("SecurityController.postClientRegistration / - clientRegistrationRequest {}", clientRegistrationRequest);
        ClientRegistrationResponse clientRegistrationResponse = userService.postNewUser(clientRegistrationRequest);

        return ResponseEntity.status(HttpStatus.CREATED).body(clientRegistrationResponse);
    }

    @Operation(//
            summary = "Регистрация организации",
            description = "Позволяет сохранить новую организацию в базе данных"
    )
    @PostMapping("/organization")
    public ResponseEntity<MessageCreateResponse> postOrganizationRegistration(@Parameter(description = "Данные организации") @Valid @RequestBody OrganizationRegistrationRequest organizationRegistrationRequest,
                                                                              @Parameter(description = "Тело запроса клиента")HttpServletRequest request,
                                                                              @Parameter(description = "токен пользователя") @RequestHeader("Authorization") String authorization) {
        log.trace("SecurityController.postOrganizationRegistration /organization - organizationRegistrationRequest {}", organizationRegistrationRequest);
        ipAddressUtils.checkIpInAdministrator(request, authorization);
        MessageCreateResponse messageCreateResponse = userService.postNewOrganization(organizationRegistrationRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(messageCreateResponse);
    }

    @Operation(//
            summary = "Активация аккаунта",
            description = "Позволяет изменить данные пользователя, чтобы он был активирован"
    )
    @PutMapping("/active")
    public ResponseEntity<ClientRegistrationResponse> putActiveUser(@Parameter(description = "Данные для активации") @Valid @RequestBody ActiveClientRequest activeClientRequest) {
        log.trace("SecurityController.putActiveUser /active - activeClientRequest {}", activeClientRequest);
        ClientRegistrationResponse clientRegistrationResponse = userService.putActiveUser(activeClientRequest);

        return ResponseEntity.accepted().body(clientRegistrationResponse);
    }


}
