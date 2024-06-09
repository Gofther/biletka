package biletka.main.service;

import biletka.main.dto.request.ActiveClientRequest;
import biletka.main.dto.request.AuthForm;
import biletka.main.dto.request.ClientRegistrationRequest;
import biletka.main.dto.request.OrganizationRegistrationRequest;
import biletka.main.dto.response.AuthResponse;
import biletka.main.dto.response.ClientRegistrationResponse;
import biletka.main.dto.response.MessageCreateResponse;
import biletka.main.entity.Users;
import jakarta.mail.MessagingException;
import org.springframework.stereotype.Service;

import java.text.ParseException;

@Service
public interface UserService {

    /**
     * Метод получения токена авторизации
     * @param authForm форма аутентификации
     * @return токен авторизации
     */
    AuthResponse getAuthToken(AuthForm authForm);

    /**
     * Метод сохранения нового пользователя в бд
     * @param clientRegistrationRequest данные пользователя
     * @return сообщение о успешном создании пользователя
     */
    ClientRegistrationResponse postNewUser(ClientRegistrationRequest clientRegistrationRequest) throws ParseException, MessagingException;

    /**
     * Метод сохранения нового организации в бд
     * @param organizationRegistrationRequest данные организации
     * @return сообщение о успешном создании организации
     */
    MessageCreateResponse postNewOrganization(OrganizationRegistrationRequest organizationRegistrationRequest);


    /**
     * Метод активации пользователя с помощью кода
     * @param activeClientRequest данные для активации
     * @return сообщение о успешной активации
     */
    ClientRegistrationResponse putActiveUser(ActiveClientRequest activeClientRequest);

    /**
     * Метод получения пользователя по почте
     * @param userEmail почта пользователя
     * @return данные пользователя
     */
    Users getUserByEmail(String userEmail);

    /**
     * Метод получения пользователя организации по почте
     * @param userEmail почта пользователя
     * @return данные пользователя
     */
    Users getUserOrganizationByEmail(String userEmail);
}
