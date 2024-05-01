package biletka.main.service.Impl;

import biletka.main.entity.Actor;
import biletka.main.repository.ActorRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import biletka.main.service.ActorService;

@Service
@RequiredArgsConstructor
@Slf4j
public class ActorServiceImpl implements ActorService{
    private final ActorRepository actorRepository;

    @Override
    public Actor getActorOfName(String name){
        return actorRepository.findFirstByName(name);
    }
    @Override
    public Actor getActorOfId(Long id){
        Actor actor = actorRepository.getReferenceById(Long.valueOf(id));
        return actor;
    }
}
