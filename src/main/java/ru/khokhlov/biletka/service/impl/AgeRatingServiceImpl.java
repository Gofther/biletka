package ru.khokhlov.biletka.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.khokhlov.biletka.entity.AgeRating;
import ru.khokhlov.biletka.repository.AgeRatingRepository;
import ru.khokhlov.biletka.service.AgeRatingService;

@Service
@RequiredArgsConstructor
@Slf4j
public class AgeRatingServiceImpl implements AgeRatingService {
    private final AgeRatingRepository ageRatingRepository;


    @Override
    public void createAgeRating(int limit) {
        log.trace("AgeRatingServiceImpl.createAgeRating - limit {}", limit);

        if(ageRatingRepository.findFirstByLimitation(limit) == null){
           AgeRating ageRating = new AgeRating();
           ageRating.setLimitation(limit);
           ageRatingRepository.saveAndFlush(ageRating);
       }
    }

    @Override
    public AgeRating getAgeRating(int limit) {
        log.trace("AgeRatingServiceImpl.getAgeRating - limit {}", limit);
        return ageRatingRepository.findFirstByLimitation(limit);
    }

    @Override
    public Long getIdByLimitation(int limit) {
        log.trace("AgeRatingServiceImpl.getIdByLimitation - limit {}", limit);
        return ageRatingRepository.findFirstByLimitation(limit) != null ? ageRatingRepository.findFirstByLimitation(limit).getId() : -1;
    }

    /**
     * Функция для получения числового ограничения возраста из строки
     * @param limit строковое ограничение
     * @return -1 в случае некорректного ввода числа, иначе будет возвращено численное ограничение
     */
    public int getLimitationByString(String limit) {
        log.trace("AgeRatingServiceImpl.getLimitationByString - limit {}", limit);

        int result = -1;

        if (limit != null ){
            try {
                result = Integer.parseInt(limit);

                if (!(result > 0 && result < 99)) result = -1;
            }
            catch (NumberFormatException e){
                log.error("AgeRatingServiceImpl.getLimitationByString - Error - {}", e.toString());
            }
        }

        return result;
    }
}
