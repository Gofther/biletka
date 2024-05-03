package biletka.main.service.Impl;

import biletka.main.entity.Actor;
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

    @Override
    public TypeEvent getTypeEventOfId(Long id){
        TypeEvent typeEvent = typeEventRepository.getReferenceById(Long.valueOf(id));
        return typeEvent;
    }

    @Override
    public TypeEvent getTypeEventOfName(String type){
        TypeEvent typeEvent = typeEventRepository.findFirstByType(type);
        return typeEvent;
    }

    /**
     * Метод Создания нового типа мероприятия и сохранения в бд
     * @param type - тип мероприятия
     * @return Созданный тип мероприятия
     */
    @Override
    public TypeEvent PostNewTypeEvent(String type){
        TypeEvent typeEvent = typeEventRepository.findFirstByType(type);
        if(typeEvent!= null){
            List<ErrorMessage> errorMessages = new ArrayList<>();
            errorMessages.add(new ErrorMessage("Post error", "This actor already exists!"));
            throw new InvalidDataException(errorMessages);
        }
        TypeEvent newTypeEvent = new TypeEvent(type);
        typeEventRepository.saveAndFlush(newTypeEvent);
        return newTypeEvent;
    }
}
