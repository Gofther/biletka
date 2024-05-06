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

    /**
     * Метод получения возрастного ограничения
     * @param limitation возраст
     * @return возрастное ограничение
     */
    @Override
    public AgeRating getAgeRatingOfLimitation(int limitation){
        log.trace("AgeRatingServiceImpl.getAgeRatingOfLimitation - limitation {}", limitation);
        return ageRatingRepository.findFirstByLimitation(limitation);
    }
}
