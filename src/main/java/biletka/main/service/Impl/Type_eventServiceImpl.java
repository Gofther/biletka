package biletka.main.service.Impl;

import biletka.main.entity.Type_event;
import biletka.main.repository.Type_eventRepository;
import biletka.main.service.Type_eventService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class Type_eventServiceImpl implements Type_eventService {
    private final Type_eventRepository type_eventRepository;

    @Override
    public Type_event getType_eventOfId(Long id){
        Type_event type_event = type_eventRepository.getReferenceById(Long.valueOf(id));
        return type_event;
    }

    @Override
    public Type_event getType_eventOfName(String type){
        Type_event type_event = type_eventRepository.findFirstByType(type);
        return type_event;
    }
}
