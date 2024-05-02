package biletka.main.service.Impl;

import biletka.main.entity.AgeRating;
import biletka.main.repository.AgeRatingRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import biletka.main.service.AgeRatingService;

@Service
@RequiredArgsConstructor
@Slf4j
public class AgeRatingServiceImpl implements AgeRatingService {
    private final AgeRatingRepository ageRatingRepository;

    @Override
    public AgeRating getAge_ratingOfId(Long id){
        AgeRating age_rating = ageRatingRepository.getReferenceById(Long.valueOf(id));
        return age_rating;
    }

    @Override
    public AgeRating getAge_ratingOfLimitation(int limitation){
        AgeRating age_rating = ageRatingRepository.findFirstByLimitation(limitation);
        return age_rating;
    }
}
