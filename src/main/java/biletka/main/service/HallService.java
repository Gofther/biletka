package biletka.main.service;

import biletka.main.dto.request.HallCreateRequest;
import biletka.main.dto.response.MessageCreateResponse;
import biletka.main.entity.Hall;
import biletka.main.entity.Place;
import jakarta.mail.MessagingException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Set;

@Service
public interface HallService {
    /**
     * Метод создания зала площадки без схемы (требуется подтверждение администратор)
     * @param authorization токен авторизации
     * @param file схема зала
     * @param hallCreateRequestNew информация о зале
     * @return сообщение о успешном создании зала
     */
    MessageCreateResponse createHall(String authorization, MultipartFile file, HallCreateRequest hallCreateRequestNew) throws MessagingException;

    /**
     * Метод получения зала по id
     * @param id зала
     * @return зал
     */
    Hall getHallById(Long id);

    /**
     * Метод получения количества залов в площадке
     * @param place площадка
     * @return количество залов
     */
    Integer getTotalByPlace(Place place);

    /**
     * Метод получения массива залов по площадке
     * @param place площадка
     * @return массив залов
     */
    Set<Hall> getAllHallByPlace(Place place);
}
