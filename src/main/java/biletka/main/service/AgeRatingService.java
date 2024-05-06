package biletka.main.service;

import biletka.main.entity.AgeRating;
import org.springframework.stereotype.Service;

@Service
public interface AgeRatingService {
    /**
     * Метод получения возрастного ограничения
     * @param limitation возраст
     * @return возрастное ограничение
     */
    AgeRating getAgeRatingOfLimitation(int limitation);
}

