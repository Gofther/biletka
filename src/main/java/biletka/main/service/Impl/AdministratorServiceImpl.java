package biletka.main.service.Impl;

import biletka.main.Utils.JwtTokenUtils;
import biletka.main.Utils.PasswordEncoder;
import biletka.main.dto.request.AuthForm;
import biletka.main.dto.response.AuthResponse;
import biletka.main.entity.Administrator;
import biletka.main.exception.ErrorMessage;
import biletka.main.exception.InvalidDataException;
import biletka.main.repository.AdministratorRepository;
import biletka.main.service.AdministratorService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.util.matcher.IpAddressMatcher;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdministratorServiceImpl implements AdministratorService {
    private final AdministratorRepository administratorRepository;
    private final JwtTokenUtils jwtTokenUtils;

    /**
     * Метод аутентификации администратор
     * @param authForm форма аутентификации
     * @return токен для авторизации
     */
    @Override
    public AuthResponse postAuth(AuthForm authForm, HttpServletRequest request) {
        log.trace("AdministratorServiceImpl.postAuth - authForm {}", authForm);
        Administrator administrator = administratorRepository.findFirstByEmailAndAddress(authForm.email(), request.getRemoteAddr());

        if (administrator == null ||
                administrator.getStatus().getStatusUser().equalsIgnoreCase("INACTIVE") ||
                administrator.getStatus().getStatusUser().equalsIgnoreCase("ACTIVE") &&
                        (
                                !administrator.getRole().getAuthority().equalsIgnoreCase(authForm.role()) ||
                                        PasswordEncoder.arePasswordsEquals(administrator.getPassword(), authForm.password())
                        )
        ) {
            List<ErrorMessage> errorMessages = new ArrayList<>();
            errorMessages.add(new ErrorMessage("Authentication error", "The email or password is incorrect!"));
            throw new InvalidDataException(errorMessages);
        }
        // Проверка на бан аккаунта
        else if (administrator.getStatus().getStatusUser().equalsIgnoreCase("BANNED")) {
            List<ErrorMessage> errorMessages = new ArrayList<>();
            errorMessages.add(new ErrorMessage("Banned", "The account is banned!"));
            throw new InvalidDataException(errorMessages);
        }

        UserDetails userDetails = new User(
                administrator.getEmail(),
                administrator.getPassword(),
                Collections.singleton(administrator.getRole())
        );

        return new AuthResponse(
                jwtTokenUtils.generateToken(userDetails)
        );
    }

    @Override
    public Administrator getAdminByEmail(String email) {
        return administratorRepository.findFirstByEmail(email);
    }
}
