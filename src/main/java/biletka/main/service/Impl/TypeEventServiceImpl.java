package biletka.main.service.Impl;

import biletka.main.entity.TypeEvent;
import biletka.main.exception.ErrorMessage;
import biletka.main.exception.InvalidDataException;
import biletka.main.repository.TypeEventRepository;
import biletka.main.service.TypeEventService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class TypeEventServiceImpl implements TypeEventService {
    private final TypeEventRepository typeEventRepository;

    /**
     * Метод получения типа мероприятия
     * @param type название типа мероприяти
     * @return тип мероприятия
     */
    @Override
    public TypeEvent getTypeEventOfName(String type){
        log.trace("TypeEventServiceImpl.getTypeEventOfName - type {}", type);

        return typeEventRepository.findFirstByType(type);
    }

    /**
     * Метод создания типа мероприятия
     * @param type навзание типа мероприятия
     * @return тип мероприятия
     */
    @Override
    public TypeEvent createTypeEvent(String type) {
        log.trace("TypeEventServiceImpl.createTypeEvent - type {}", type);
        TypeEvent typeEvent = typeEventRepository.findFirstByType(type);

        if (typeEvent != null) {
            List<ErrorMessage> errorMessages = new ArrayList<>();
            errorMessages.add(new ErrorMessage("Genre error", "This genre already exists!"));
            throw new InvalidDataException(errorMessages);
        }

        TypeEvent typeEventNew = new TypeEvent(type);

        typeEventRepository.saveAndFlush(typeEventNew);

        return typeEventNew;
    }
}
