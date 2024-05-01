package biletka.main.service;

import biletka.main.entity.Actor;
import org.springframework.stereotype.Service;

@Service
public interface ActorService {
    Actor getActorOfName(String name);
    Actor getActorOfId(Long id);

}
