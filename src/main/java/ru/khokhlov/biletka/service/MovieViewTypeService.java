package ru.khokhlov.biletka.service;

import org.springframework.stereotype.Service;
import ru.khokhlov.biletka.entity.MovieViewType;

@Service
public interface MovieViewTypeService {
    /**
     * Функция создания типа фильма
     *
     * @param type тип фильма
     * @return объект типа фильма
     */
    MovieViewType createMovieViewType(String type);

    /**
     * Функция получения типа фильма по строке
     *
     * @param type тип фильма
     * @return объект типа фильма
     */
    MovieViewType getFirstByType(String type);
}
