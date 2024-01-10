package ru.khokhlov.biletka.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import ru.khokhlov.biletka.dto.request.ClientRegistration;
import ru.khokhlov.biletka.dto.request.UserId;
import ru.khokhlov.biletka.dto.response.ClientResponse;
import ru.khokhlov.biletka.dto.universal.PublicClient;

@Service
public interface ClientService {
    Long getClientIdByEmailAndPassword(String email, String password);

    /**
     * Функция создания пользователя
     *
     * @param clientRegistration регистрационна информация
     * @return ответная информация
     */
    ClientResponse createClient(ClientRegistration clientRegistration);

    /**
     * Функция получения деталей пользователя
     *
     * @param username имя пользователя
     * @return детали
     */
    UserDetails loadUserByUsername(String username);

    /**
     * Функция подтверждения почты клиента
     *
     * @param code код активации
     * @return true - подтверждена почта
     */
    boolean isEmailActivate(String code);

    PublicClient infoClient(Integer id);
}
