package ru.khokhlov.biletka.service;

import org.springframework.stereotype.Service;
import ru.khokhlov.biletka.dto.request.event_full_ei.DurationRequest;
import ru.khokhlov.biletka.entity.EventDuration;

@Service
public interface DurationService {
    /**
     * Функция создания продолжительности Event
     *
     * @param durationRequest продолжительность в часах и минутах
     * @return удчаное/неудачное создание продолжительности
     */
    EventDuration createDuration(DurationRequest durationRequest);
}
