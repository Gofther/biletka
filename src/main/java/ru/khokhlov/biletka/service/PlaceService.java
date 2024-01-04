package ru.khokhlov.biletka.service;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import ru.khokhlov.biletka.dto.request.PlaceInfo;
import ru.khokhlov.biletka.dto.response.PlaceResponse;
import ru.khokhlov.biletka.entity.City;
import ru.khokhlov.biletka.entity.Place;

import java.util.List;
import java.util.Set;

@Service
public interface PlaceService {
    /**
     * Функция создания площадки
     *
     * @param city название города
     * @param placeInfo информация о площадке
     * @return информация о площадке
     */
    PlaceResponse createPlace(String city, PlaceInfo placeInfo);

    /**
     * Функция получения площадки по id
     *
     * @param placeId номер площадки
     * @return информация о площадке
     */
    Place getPlaceById(Long placeId) throws EntityNotFoundException;


    /**
     * Функция получения площадки
     * @param city название города
     * @return возвращает площадку
     */
    Place getPlaceByCity(String city);

    /**
     * Функция получения площадки
     *
     * @param name название площадки
     * @param city название города
     * @return возвращает площадку
     */
    Place getPlaceByNameAndCity(String name, String city);

    /**
     * Функция получения всех площадок организации
     *
     * @param city город
     * @param organizationId id организации
     * @return множество площадок организации
     */
    List<Place> getAllPlaceByOrganization(String city, Long organizationId) throws EntityNotFoundException;


    /**
     * Функция получения всех площадок в городе
     *
     * @param city город
     * @return множество площадок в городе
     */
    Set<Place> getAllByCity(City city);
}
