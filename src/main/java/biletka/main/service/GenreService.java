package biletka.main.service;

import biletka.main.entity.Genre;
import org.springframework.stereotype.Service;

@Service
public interface GenreService {
    Genre getGenreOfId(Long id);

    Genre getGenreOfName(String name);

    /**
     * Метод Создания нового жанра и сохранения в бд
     * @param name - название жанра
     * @return Созданный жанр
     */
    Genre PostNewGenre(String name);
}
