package biletka.main.service;

import biletka.main.dto.request.HallCreateRequest;
import biletka.main.dto.response.MessageCreateResponse;
import biletka.main.entity.Hall;
import jakarta.mail.MessagingException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

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
}
