package biletka.main.service.Impl;

import biletka.main.entity.City;
import biletka.main.repository.CityRepository;
import biletka.main.service.CityService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class CityServiceImpl implements CityService {
    private final CityRepository cityRepository;

    /**
     * Метод получения города по английскому названию
     * @param name название города
     * @return город
     */
    @Override
    public City getCityByNameEng(String name) throws EntityNotFoundException {
        City city = cityRepository.findFirstByCityNameEng(name);

        if (city == null) {
            throw new EntityNotFoundException("City not found!");
        }

        return city;
    }
}
