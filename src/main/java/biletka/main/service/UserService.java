package biletka.main.service;

import biletka.main.dto.request.AuthForm;
import biletka.main.dto.response.AuthResponse;
import org.springframework.stereotype.Service;

@Service
public interface UserService {

    /**
     * Метод получения токена авторизации
     * @param authForm форма аутентификации
     * @return токен авторизации
     */
    AuthResponse getAuthToken(AuthForm authForm);
}
