package ru.khokhlov.biletka.controller;

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import ru.khokhlov.biletka.dto.request.AuthFormDTO;
import ru.khokhlov.biletka.dto.request.ClientRegistration;
import ru.khokhlov.biletka.dto.request.OrganizationRegistration;
import ru.khokhlov.biletka.dto.request.UserId;
import ru.khokhlov.biletka.dto.response.ClientResponse;
import ru.khokhlov.biletka.dto.response.ExitResponse;
import ru.khokhlov.biletka.dto.response.OrganizationResponse;
import ru.khokhlov.biletka.dto.response.Token;
import ru.khokhlov.biletka.dto.universal.PublicClient;
import ru.khokhlov.biletka.dto.universal.PublicOrganization;
import ru.khokhlov.biletka.service.ClientService;
import ru.khokhlov.biletka.service.OrganizationService;
import ru.khokhlov.biletka.utils.JwtTokenUtils;


import java.security.Principal;

@Slf4j
@RestController
@RequestMapping("/security")
@RequiredArgsConstructor
@Tag(name = "Контроллер безопасности", description = "Всё, что связанно с безопасностью")
public class SecurityController {
    public final OrganizationService organizationService;
    private final ClientService clientService;
    private final JwtTokenUtils jwtTokenUtils;
    private final AuthenticationManager authenticationManager;

    @Operation(
            summary = "Регистрация пользователя",
            description = "Позволяет зарегистрировать пользователя")
    @PostMapping(value = "/client")
    public ResponseEntity<ClientResponse> createClient(@Parameter(description = "Информация о клиенте") @Valid @RequestBody ClientRegistration client) {
        return ResponseEntity.status(HttpStatus.CREATED).body(clientService.createClient(client));
    }

    @Operation(
            summary = "Создание организации",
            description = "Занесение юридического лица в базу данных")
    @PostMapping(value = "/organization")
    public ResponseEntity<OrganizationResponse> createOrganization(@Parameter(description = "Информация об организации для регистрации") @Valid @RequestBody OrganizationRegistration organizationRegistration) {
        log.trace("OrganizationController.createOrganization /organization - organizationRegistration {}", organizationRegistration);
        OrganizationResponse organization = organizationService.createOrganization(organizationRegistration);

        return ResponseEntity.status(HttpStatus.CREATED).body(organization);
    }

    @Hidden
    @Operation(
            summary = "Получение токена авторизации",
            description = "Позволяет получить токен для взаимодействия с системой")
    @PostMapping("/auth")
    public ResponseEntity<Token> createAuthToken(@Parameter(description = "Форма авторизации") @Valid @RequestBody AuthFormDTO authFormDTO) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authFormDTO.email(), authFormDTO.password()));

        Long clientId = clientService.getClientIdByEmailAndPassword(authFormDTO.email(), authFormDTO.password());
        Long organizationId = organizationService.getOrganizationIdByEmailAndPassword(authFormDTO.email(), authFormDTO.password());

        UserDetails userDetails = clientService.loadUserByUsername(authFormDTO.email());
        String token = jwtTokenUtils.generateToken(userDetails);

        return ResponseEntity.status(HttpStatus.OK).body(new Token(clientId != -1L ? clientId : organizationId, clientId != -1L ? "USER": "ORGAANIZATION", "Bearer", token));
    }

    /**
     * Marks a token as invalid, making it no longer usable.
     *
     * @param authorizationHeader The authorization header with the token to invalidate.
     * @param principal           The principal user.
     * @return An ExitResponse indicating the success of token invalidation.
     */
    @Operation(
            summary = "Invalidate Authorization Token",
            description = "Marks a token as invalid, making it no longer usable."
    )
    @PutMapping("/invalidation")
    public ResponseEntity<ExitResponse> invalidateToken(
            @RequestHeader("Authorization") String authorizationHeader,
            @Parameter(description = "The current user") Principal principal) {
        ExitResponse exitResponse = new ExitResponse(principal.getName());
        jwtTokenUtils.invalidateToken(authorizationHeader);

        return ResponseEntity.status(HttpStatus.OK).body(exitResponse);
    }

    @Hidden
    @GetMapping("/activate/{code}")
    public String activation(@PathVariable String code) {
        //TODO: 19.10.2023 Подумать как отправлять: Такой страницы не существует
        boolean isActivated = clientService.isEmailActivate(code);
        String message = "Такого кода активации не существует!";

        if (isActivated) {
            message = "Вы подтвердили свой e-mail и можете закрывать эту страницу";
        }

        return message;
    }

    @GetMapping("/client/{id}")
    public ResponseEntity<PublicClient> infoClient(@PathVariable Integer id) {
        log.trace("SecurityController.infoClient /client - id {}", id);
        return ResponseEntity.status(HttpStatus.OK).body(clientService.infoClient(id));
    }

    @GetMapping("/organization/{id}")
    public ResponseEntity<PublicOrganization> infoOrganization(@PathVariable Integer id) {
        log.trace("SecurityController.infoOrganization /organization - id {}", id);
        return ResponseEntity.status(HttpStatus.OK).body(organizationService.infoOrganization(id));
    }
}