package biletka.main.service;

import biletka.main.dto.response.MassiveTypeResponse;
import biletka.main.entity.TypeEvent;
import org.springframework.stereotype.Service;

@Service
public interface TypeEventService {
    TypeEvent getTypeEventOfName(String type);

    /**
     * Метод создания типа мероприятия
     * @param type навзание типа мероприятия
     * @return тип мероприятия
     */
    TypeEvent createTypeEvent(String type);

    /**
     * Метод вывода всех возможных типов мероприятия
     * @return массив типов
     */
    MassiveTypeResponse getAllType();
}
