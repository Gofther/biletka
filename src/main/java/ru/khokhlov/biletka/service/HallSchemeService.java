package ru.khokhlov.biletka.service;

import org.springframework.stereotype.Service;
import ru.khokhlov.biletka.dto.request.HallCreationRequestDTO;
import ru.khokhlov.biletka.dto.response.HallCreationResponseDTO;
import ru.khokhlov.biletka.dto.response.SchemeResponse;
import ru.khokhlov.biletka.entity.HallScheme;

import java.util.List;

@Service
public interface HallSchemeService {
    /**
     * Функция получения HallScheme
     *
     * @param hallNumber номер зала
     * @return схема зала
     */
    HallScheme getHallScheme(Long hallNumber);

    /**
     * Функция создания HallScheme
     *
     * @param city город
     * @param hallCreationRequestDTO данные для создания схемы
     * @return основные параметры созданной схемы
     */
    HallCreationResponseDTO createHallScheme(String city, HallCreationRequestDTO hallCreationRequestDTO);

    /**
     * Функция получения всех HallScheme у организации
     *
     * @param city город
     * @param organizationId id организации
     * @return список схем залов организации
     */
    List<HallScheme> getAllHallByOrganization(String city, Long organizationId);

    String putHall(String scheme, Long id);

    SchemeResponse getScheme(Long hallId);
}
