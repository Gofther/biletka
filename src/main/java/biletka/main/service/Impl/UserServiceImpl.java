package biletka.main.service.Impl;

import biletka.main.Utils.JwtTokenUtils;
import biletka.main.Utils.PasswordEncoder;
import biletka.main.dto.request.AuthForm;
import biletka.main.dto.response.AuthResponse;
import biletka.main.entity.Users;
import biletka.main.enums.RoleEnum;
import biletka.main.exception.ErrorMessage;
import biletka.main.exception.InvalidDataException;
import biletka.main.repository.UserRepository;
import biletka.main.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService, UserDetailsService {
    private final UserRepository userRepository;
    private final JwtTokenUtils jwtTokenUtils;

    /**
     * Функция получения id пользователя по почте и паролю
     * @param email почта пользователя
     * @param password пароль пользователя
     * @return id пользователя
     */
    @Override
    public Long getClientIdByEmailAndPassword(String email, String password) {
        Users user = userRepository.findFirstByEmail(email);

        return user.getId();
    }

    /**
     * Метод получения токена авторизации
     * @param authForm форма аутентификации
     * @return токен авторизации
     */
    @Override
    @Transactional
    public AuthResponse getAuthToken(AuthForm authForm) {
        Users user = userRepository.findFirstByEmail(authForm.email());

        // Проверка на правильно введнные данные и не актвиный аккаунт
        if (user == null ||
                user.getStatus().getStatusUser().equalsIgnoreCase("INACTIVE") ||
                user.getStatus().getStatusUser().equalsIgnoreCase("ACTIVE") &&
                (
                        !user.getRole().getAuthority().equalsIgnoreCase(authForm.role()) ||
                        PasswordEncoder.arePasswordsEquals(user.getPassword(), authForm.password())
                )
        ) {
            List<ErrorMessage> errorMessages = new ArrayList<>();
            errorMessages.add(new ErrorMessage("Authentication error", "The email or password is incorrect!"));
            throw new InvalidDataException(errorMessages);
        }
        // Проверка на бан аккаунта
        else if (user.getStatus().getStatusUser().equalsIgnoreCase("BANNED")) {
            List<ErrorMessage> errorMessages = new ArrayList<>();
            errorMessages.add(new ErrorMessage("Banned", "The account is banned!"));
            throw new InvalidDataException(errorMessages);
        }

        // Детали пользователя для токена
        UserDetails userDetails = new User(
                user.getEmail(),
                user.getPassword(),
                Collections.singleton(user.getRole())
        );

        return new AuthResponse(
                jwtTokenUtils.generateToken(userDetails)
        );
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException, EntityNotFoundException {
        log.trace("UserServiceImpl.loadUserByUsername - username {}", email);

        Users user = userRepository.findFirstByEmail(email);

        if (user == null) {
            throw new EntityNotFoundException("Entity with email " + email + " not found");
        }

        return new User(
                user.getEmail(),
                user.getPassword(),
                Collections.singleton(user.getRole())
        );
    }

}
