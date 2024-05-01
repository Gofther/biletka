package biletka.main.service;

import biletka.main.entity.Age_rating;
import org.springframework.stereotype.Service;

@Service
public interface Age_ratingService {
    Age_rating getAge_ratingOfId(Long id);
    Age_rating getAge_ratingOfLimitation(int limitation);
}

