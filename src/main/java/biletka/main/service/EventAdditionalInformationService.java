package biletka.main.service;

import biletka.main.dto.request.event_item.EventAdditional;
import biletka.main.entity.event_item.EventAdditionalInformation;
import org.springframework.stereotype.Service;

@Service
public interface EventAdditionalInformationService {
    /**
     * Метод создания и сохрания дополнительной ифнормации в бд
     * @param eventAdditional дополнительная информация
     * @return дополнительная информация мероприятия
     */
    EventAdditionalInformation createEventAdditional(EventAdditional eventAdditional);
}
