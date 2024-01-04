package ru.khokhlov.biletka.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.khokhlov.biletka.entity.City;
import ru.khokhlov.biletka.repository.CityRepository;
import ru.khokhlov.biletka.service.CityService;

@Slf4j
@Service
@RequiredArgsConstructor
public class CityServiceImpl implements CityService {
    private final CityRepository repository;

    @Override
    public City getCity(String engName) throws EntityNotFoundException {
        City city = repository.findFirstByNameEng(engName);

        if (city == null) {
            throw new EntityNotFoundException("City with the name " + engName + " not found!");
        }

        return city;
    }
}
