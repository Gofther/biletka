package biletka.main.service;

import biletka.main.entity.City;
import org.springframework.stereotype.Service;

@Service
public interface CityService {
    /**
     * Метод получения города по английскому названию
     * @param name название города
     * @return город
     */
    City getCityByNameEng(String name);
}
