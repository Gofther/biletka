package biletka.main.service;

import biletka.main.entity.AgeRating;
import org.springframework.stereotype.Service;

@Service
public interface AgeRatingService {
    AgeRating getAge_ratingOfId(Long id);
    AgeRating getAge_ratingOfLimitation(int limitation);
}

