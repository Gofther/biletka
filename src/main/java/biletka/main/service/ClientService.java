package biletka.main.service;

import biletka.main.dto.request.ClientRegistrationRequest;
import biletka.main.entity.Users;
import org.springframework.stereotype.Service;

import java.text.ParseException;

@Service
public interface ClientService {
    /**
     * Метод добавления нового пользователя в бд
     * @param clientRegistrationRequest данные пользователя
     * @param user данные для входа
     */
    void  postNewClient(ClientRegistrationRequest clientRegistrationRequest, Users user) throws ParseException;
}
