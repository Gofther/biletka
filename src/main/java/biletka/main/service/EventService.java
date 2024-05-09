package biletka.main.service;

import biletka.main.dto.request.EventCreateRequest;
import biletka.main.dto.response.MessageCreateResponse;
import biletka.main.entity.Event;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public interface EventService {
    /**
     * Метод создания и добавления мероприятия в бд
     * @param authorization токен авторизации
     * @param file изображение мероприятия
     * @param eventCreateRequest информация мероприятия
     * @return сообщение оуспешном создании мероприятия
     */
    MessageCreateResponse createEvent(String authorization, MultipartFile file, EventCreateRequest eventCreateRequest) throws IOException;

    /**
     * Метод получения мероприятия по id
     * @param id мероприятия
     * @return мероприятие
     */
    Event getEventById(Long id);
}
