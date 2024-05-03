package biletka.main.service;

import biletka.main.entity.AgeRating;
import org.springframework.stereotype.Service;

@Service
public interface AgeRatingService {
    AgeRating getAge_ratingOfId(Long id);
    AgeRating getAge_ratingOfLimitation(int limitation);

    /**
     * Метод Создания нового возрастного ограничения и сохранения в бд
     * @param limitation - возрастное ограничение
     * @return Созданное возрастное ограничение
     */
    AgeRating PostNewAgeRating(int limitation);
}

