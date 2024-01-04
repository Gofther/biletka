package ru.khokhlov.biletka.service;

import org.springframework.stereotype.Service;
import ru.khokhlov.biletka.entity.Tag;

@Service
public interface TagService {
    /**
     * Функция создания тега
     * @param name имя тега
     */
    void createTag(String name);

    /**
     * Получения тега по имени
     *
     * @param name имя тега
     * @return тег
     */
    Tag getTagByName(String name);
}
