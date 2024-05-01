package biletka.main.service.Impl;

import biletka.main.entity.Age_rating;
import biletka.main.repository.Age_ratingRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import biletka.main.service.Age_ratingService;

@Service
@RequiredArgsConstructor
@Slf4j
public class Age_ratingServiceImpl implements Age_ratingService{
    private final Age_ratingRepository ageRatingRepository;

    @Override
    public Age_rating getAge_ratingOfId(Long id){
        Age_rating age_rating = ageRatingRepository.getReferenceById(Long.valueOf(id));
        return age_rating;
    }

    @Override
    public Age_rating getAge_ratingOfLimitation(int limitation){
        Age_rating age_rating = ageRatingRepository.findFirstByLimitation(limitation);
        return age_rating;
    }
}
