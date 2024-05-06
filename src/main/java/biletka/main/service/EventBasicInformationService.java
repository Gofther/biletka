package biletka.main.service;

import biletka.main.dto.request.event_item.EventBasicRequest;
import biletka.main.entity.event_item.EventBasicInformation;
import org.springframework.stereotype.Service;

@Service
public interface EventBasicInformationService {
    /**
     * Метод создания и сохранения основной информации о мероприятии в бд
     * @param eventBasicRequest основная информация для создания
     * @return основная информация о мероприятии
     */
    EventBasicInformation createEventBasic(EventBasicRequest eventBasicRequest, String fullNameFile);
}
