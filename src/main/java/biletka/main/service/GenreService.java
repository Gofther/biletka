package biletka.main.service;

import biletka.main.entity.Genre;
import org.springframework.stereotype.Service;

@Service
public interface GenreService {
    /**
     * Метод поиска жанра
     * @param name название жанра
     * @return жанр
     */
    Genre getGenreOfName(String name);

    /**
     * Метод создания жанра
     * @param name название жанра
     * @return жанр
     */
    Genre createGenre(String name);

    Genre postNewGenre(String name);
}
