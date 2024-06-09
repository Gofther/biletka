package biletka.main.service;

import biletka.main.dto.response.MassiveCityResponse;
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

    /**
     * Метод получения всех городов
     * @return массив городов
     */
    MassiveCityResponse getAllCity();
}
