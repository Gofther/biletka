package ru.khokhlov.biletka.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.khokhlov.biletka.entity.EventType;
import ru.khokhlov.biletka.repository.EventTypeRepository;
import ru.khokhlov.biletka.service.EventTypeService;

@Service
@RequiredArgsConstructor
@Slf4j
public class EventTypeServiceImpl implements EventTypeService {
    private final EventTypeRepository eventTypeRepository;

    @Override
    public void createEventType(String type) {
        log.trace("EventTypeServiceImpl.createEventType - type {}", type);

        if (eventTypeRepository.findFirstByType(type) == null){
            EventType eventType = new EventType();
            eventType.setType(type);
            eventTypeRepository.saveAndFlush(eventType);
        }
    }

    @Override
    public EventType getEventType(String type) {
        log.trace("EventTypeServiceImpl.getEventType - type {}", type);

        return eventTypeRepository.findFirstByType(type);
    }

    @Override
    public Long getIdByType(String type) {
        log.trace("EventTypeServiceImpl.getIdByType - type {}", type);

        return eventTypeRepository.findFirstByType(type) != null ? eventTypeRepository.findFirstByType(type).getTypeId() : -1;
    }
}
