package ru.khokhlov.biletka.service;

import ru.khokhlov.biletka.entity.City;

public interface CityService {
    /**
     * Функция получения города по английскому имени
     *
     * @param engName английское имя
     * @return город
     */
    City getCity(String engName);
}
