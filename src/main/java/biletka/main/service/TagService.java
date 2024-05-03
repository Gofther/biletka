package biletka.main.service;

import biletka.main.entity.Tag;
import org.springframework.stereotype.Service;

@Service
public interface TagService {
    Tag getTagOfId(Long id);

    Tag getTagOfName(String name);

    /**
     * Метод Создания нового тага и сохранения в бд
     * @param name - имя актёра
     * @return Созданный актёр
     */
    Tag PostNewTag(String name);
}
