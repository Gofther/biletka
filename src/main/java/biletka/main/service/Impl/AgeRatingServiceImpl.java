package biletka.main.service.Impl;

import biletka.main.entity.AgeRating;
import biletka.main.exception.ErrorMessage;
import biletka.main.exception.InvalidDataException;
import biletka.main.repository.AgeRatingRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import biletka.main.service.AgeRatingService;

import java.util.ArrayList;
import java.util.List;

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

    /**
     * Метод Создания нового возрастного ограничения и сохранения в бд
     * @param limitation - возрастное ограничение
     * @return Созданное возрастное ограничение
     */
    @Override
    public AgeRating PostNewAgeRating(int limitation){
        AgeRating ageRating = ageRatingRepository.findFirstByLimitation(limitation);
        if(ageRating!= null){
            List<ErrorMessage> errorMessages = new ArrayList<>();
            errorMessages.add(new ErrorMessage("Post error", "This age rating already exists!"));
            throw new InvalidDataException(errorMessages);
        }
        AgeRating newAgeRating = new AgeRating(limitation);
        ageRatingRepository.saveAndFlush(newAgeRating);
        return newAgeRating;
    }
}
