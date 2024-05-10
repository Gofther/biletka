package biletka.main.service;

import biletka.main.dto.request.ClientRegistrationRequest;
import biletka.main.dto.response.FavoriteResponse;
import biletka.main.dto.universal.MassivePublicEvent;
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

    /**
     * Метод изменения мероприятия в избранном пользователя
     * @param authorization токен авторизации
     * @param id мероприятия
     * @return id и измененный статус избранного
     */
    FavoriteResponse toggleEventFavorite(String authorization, Long id);

    /**
     * Метод получения массива мероприятий из таблицы избранное у пользователя
     * @param authorization токен авторизации
     * @return массив мероприятий
     */
    MassivePublicEvent getFavorite(String authorization);
}
