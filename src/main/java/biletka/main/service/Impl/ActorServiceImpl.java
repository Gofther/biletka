package biletka.main.service.Impl;

import biletka.main.entity.Actor;
import biletka.main.exception.ErrorMessage;
import biletka.main.exception.InvalidDataException;
import biletka.main.repository.ActorRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import biletka.main.service.ActorService;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ActorServiceImpl implements ActorService{
    private final ActorRepository actorRepository;

    @Override
    public Actor getActorOfName(String name){
        log.trace("ActorService.getActorOfName - name{}", name);

        Actor actor = actorRepository.findFirstByName(name);
        return actor;
    }
    @Override
    public Actor getActorOfId(Long id){
        log.trace("ActorService.getActorOfId - id{}", id);

        Actor actor = actorRepository.getReferenceById(Long.valueOf(id));
        return actor;
    }

    /**
     * Метод Создания нового актёра и сохранения в бд
     * @param name - имя актёра
     * @return Созданный актёр
     */
    @Override
    public Actor postNewActor(String name){
        log.trace("ActorService.postNewActor - name{}", name);

        Actor actor = actorRepository.findFirstByName(name);

        if(actor!= null){
            List<ErrorMessage> errorMessages = new ArrayList<>();
            errorMessages.add(new ErrorMessage("Post error", "This actor already exists!"));
            throw new InvalidDataException(errorMessages);
        }

        Actor newActor = new Actor(name);
        actorRepository.saveAndFlush(newActor);
        return newActor;
    }
}
