package ru.khokhlov.biletka.service;

import org.springframework.stereotype.Service;
import ru.khokhlov.biletka.entity.AgeRating;

@Service
public interface AgeRatingService {
    /**
     * Функция создания рейтинга
     *
     * @param limit возрастное ограничение
     */
    void createAgeRating(int limit);

    /**
     * Функция получения объекта возрастного рейтинга
     *
     * @param limit ограничение
     * @return объект возрастного рейтинга или null
     */
    AgeRating getAgeRating(int limit);

    /**
     * Функция возврата id рейтинга из полученного ограничения
     *
     * @param limit ограничение
     * @return id рейтинга или -1 если такого ограничения нет
     */
    Long getIdByLimitation(int limit);
}
