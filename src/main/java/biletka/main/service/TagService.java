package biletka.main.service;

import biletka.main.entity.Tag;
import org.springframework.stereotype.Service;

@Service
public interface TagService {
    /**
     * Метод получения тега мероприятия
     * @param name название тега
     * @return тег
     */
    Tag getTagOfName(String name);

    /**
     * Метод создания тега
     * @param name название тега
     * @return тег
     */
    Tag createTag(String name);

    Tag postNewTag(String name);
}
