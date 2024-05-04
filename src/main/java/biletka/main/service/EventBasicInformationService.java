package biletka.main.service;

import biletka.main.dto.request.EnentItem.EventBasicInformationRequest;
import biletka.main.entity.event_item.EventBasicInformation;
import org.springframework.stereotype.Service;

@Service
public interface EventBasicInformationService {

    /**
     * Метод Создания новогой информации о событии и сохранения в бд
     * @param eventBasicInformationRequest - информация о событии
     * @return Инициализированная информация о событии
     */
    EventBasicInformation postNewEventBasicInformation(EventBasicInformationRequest eventBasicInformationRequest);
}
