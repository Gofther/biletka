package biletka.main.service;

import biletka.main.entity.Actor;
import org.springframework.stereotype.Service;

@Service
public interface ActorService {
    /**
     * Метод получения актёра
     * @param name имя актёра
     * @return актёр
     */
    Actor getActorOfName(String name);

    /**
     * Метод создания актёра
     * @param name ФИО актера
     * @return актёр
     */

    Actor postNewActor(String name);
    Actor createActor(String name);
}
