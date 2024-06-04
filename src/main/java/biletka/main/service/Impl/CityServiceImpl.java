package biletka.main.service.Impl;

import biletka.main.dto.response.CityResponse;
import biletka.main.dto.response.MassiveCityResponse;
import biletka.main.entity.City;
import biletka.main.repository.CityRepository;
import biletka.main.service.CityService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

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
        log.trace("CityServiceImpl.getCityByNameEng - name {}", name);
        City city = cityRepository.findFirstByCityNameEng(name);

        if (city == null) {
            throw new EntityNotFoundException("City not found!");
        }

        return city;
    }

    /**
     * Метод получения всех городов
     * @return массив городов
     */
    @Override
    public MassiveCityResponse getAllCity() {
        log.trace("CityServiceImpl.getAllCity");
        ArrayList<CityResponse> cityResponseArrayList = new ArrayList<>();

        cityRepository.findAll().forEach(city -> {
            cityResponseArrayList.add(
                    new CityResponse(
                            city.getId(),
                            city.getCityName(),
                            city.getCityNameEng()
                    )
            );
        });

        return new MassiveCityResponse(cityResponseArrayList.toArray(CityResponse[]::new));
    }
}
