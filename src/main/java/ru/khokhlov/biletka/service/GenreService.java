package ru.khokhlov.biletka.service;

import org.springframework.stereotype.Service;
import ru.khokhlov.biletka.entity.Genre;

import java.util.Set;

@Service
public interface GenreService {
    /**
     * Функция поиска Set<Genre> по названию
     *
     * @param genres название Genre в массиве
     * @return успешный/неуспешный поиск Set<Genre>
     */
    Set<Genre> getAllGenreByStringMassive(String[] genres);

    /**
     * Функция создания Genre по названию
     *
     * @param name название Genre
     */
    void createGenre(String name);

    /**
     * Функция поиска id Genre по названию
     *
     * @param name название Genre
     * @return успешный/неуспешный поиск Genre
     */
    Long getIdByGenre(String name);
}
