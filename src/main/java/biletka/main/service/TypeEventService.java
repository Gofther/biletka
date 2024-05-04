package biletka.main.service;

import biletka.main.entity.TypeEvent;
import org.springframework.stereotype.Service;

@Service
public interface TypeEventService {
    TypeEvent getTypeEventOfId(Long id);

    TypeEvent getTypeEventOfName(String type);

    /**
     * Метод Создания нового типа мероприятия и сохранения в бд
     * @param type - тип мероприятия
     * @return Созданный тип мероприятия
     */
    TypeEvent postNewTypeEvent(String type);
}
