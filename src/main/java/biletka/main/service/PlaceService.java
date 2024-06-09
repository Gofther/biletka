package biletka.main.service;

import biletka.main.dto.request.PlaceCreateRequest;
import biletka.main.dto.response.MessageCreateResponse;
import biletka.main.entity.Place;
import org.springframework.stereotype.Service;

@Service
public interface PlaceService {
    /**
     * Метод создания площадки для организации
     * @param authorization токен авторизации
     * @param placeCreateRequest информация для создания площадки
     * @return сообщение о успешном создании площадки
     */
    MessageCreateResponse postPlaceCreate(String authorization, PlaceCreateRequest placeCreateRequest);

    /**
     * Метод полечния площадки
     * @param id id площадки
     * @return площадка
     */
    Place getPlaceById(Long id);
}
