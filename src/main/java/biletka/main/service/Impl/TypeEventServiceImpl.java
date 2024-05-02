package biletka.main.service.Impl;

import biletka.main.entity.TypeEvent;
import biletka.main.repository.TypeEventRepository;
import biletka.main.service.TypeEventService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class TypeEventServiceImpl implements TypeEventService {
    private final TypeEventRepository type_eventRepository;

    @Override
    public TypeEvent getType_eventOfId(Long id){
        TypeEvent type_event = type_eventRepository.getReferenceById(Long.valueOf(id));
        return type_event;
    }

    @Override
    public TypeEvent getType_eventOfName(String type){
        TypeEvent type_event = type_eventRepository.findFirstByType(type);
        return type_event;
    }
}
