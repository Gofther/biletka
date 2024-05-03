package biletka.main.service.Impl;

import biletka.main.entity.Genre;
import biletka.main.exception.ErrorMessage;
import biletka.main.exception.InvalidDataException;
import biletka.main.repository.GenreRepository;
import biletka.main.service.GenreService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class GenreServiceImpl implements GenreService {
    private final GenreRepository genreRepository;

    @Override
    public Genre getGenreOfId(Long id){
        log.trace("GenreService.getGenreOfId - id{}", id);

        Genre genre = genreRepository.getReferenceById(Long.valueOf(id));
        return genre;
    }

    @Override
    public Genre getGenreOfName(String name){
        log.trace("GenreService.getGenreOfName - name{}", name);

        Genre genre = genreRepository.findFirstByName(name);
        return genre;
    }

    /**
     * Метод Создания нового жанра и сохранения в бд
     * @param name - название жанра
     * @return Созданный жанр
     */
    @Override
    public Genre postNewGenre(String name){
        log.trace("GenreService.postNewGenre - name{}", name);

        Genre genre = genreRepository.findFirstByName(name);

        if(genre!= null){
            List<ErrorMessage> errorMessages = new ArrayList<>();
            errorMessages.add(new ErrorMessage("Post error", "This genre already exists!"));
            throw new InvalidDataException(errorMessages);
        }

        Genre newGenre = new Genre(name);
        genreRepository.saveAndFlush(newGenre);
        return newGenre;
    }
}

