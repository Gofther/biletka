package ru.khokhlov.biletka.service;

import org.springframework.stereotype.Service;
import ru.khokhlov.biletka.entity.EventType;

@Service
public interface EventTypeService {
    /**
     * Функция создания типа ивента
     * @param type тип мероприятия
     */
    void createEventType(String type);

    /**
     * Функция получения объекта типа события
     * @param type - тип события
     * @return объект типа события или null
     */
    EventType getEventType(String type);

    /**
     * Функция возврата id рейтинга из полученного ограничения
     *
     * @param type тип мероприятия
     * @return id рейтинга или -1 если такого ограничения нет
     */
    Long getIdByType(String type);
}
