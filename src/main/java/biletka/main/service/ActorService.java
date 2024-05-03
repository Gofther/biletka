package biletka.main.service;

import biletka.main.entity.Actor;
import org.springframework.stereotype.Service;

@Service
public interface ActorService {
    Actor getActorOfName(String name);
    Actor getActorOfId(Long id);

    /**
     * Метод Создания нового актёра и сохранения в бд
     * @param name - имя актёра
     * @return Созданный актёр
     */
    Actor PostNewActor(String name);
}
