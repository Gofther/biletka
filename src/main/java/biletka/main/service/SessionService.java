package biletka.main.service;

import biletka.main.dto.request.SessionCreateRequest;
import biletka.main.dto.response.MessageCreateResponse;
import biletka.main.entity.City;
import biletka.main.entity.Event;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Set;

@Service
public interface SessionService {
    /**
     * Метод создания и сохранения сеанса мероприятия в бд
     * @param authorization токен авторизации
     * @param sessionCreateRequest информация о сеансе
     * @return о успешном создании сеанса
     */
    MessageCreateResponse sessionCreate(String authorization, SessionCreateRequest sessionCreateRequest);

    Set<Event> getMassiveEventByCityLimit(City city, Integer offset);
}
