package biletka.main.service.Impl;

import biletka.main.dto.response.GenreResponse;
import biletka.main.dto.response.MassiveGenreResponse;
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

    /**
     * Метод поиска жанра
     * @param name название жанра
     * @return жанр
     */
    @Override
    public Genre getGenreOfName(String name){
        log.trace("GenreServiceImpl.getGenreOfName - name {}", name);
        return genreRepository.findFirstByName(name);
    }

    /**
     * Метод создания жанра
     * @param name название жанра
     * @return жанр
     */
    @Override
    public Genre createGenre(String name) {
        log.trace("GenreServiceImpl.createGenre - name {}", name);
        Genre genre = genreRepository.findFirstByName(name);

        if (genre != null) {
            List<ErrorMessage> errorMessages = new ArrayList<>();
            errorMessages.add(new ErrorMessage("Genre error", "This genre already exists!"));
            throw new InvalidDataException(errorMessages);
        }

        Genre genreNew = new Genre(name);

        genreRepository.saveAndFlush(genreNew);

        return genreNew;
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

    /**
     * Метод получения всех жанров
     * @return массив жанров
     */
    @Override
    public MassiveGenreResponse getAllGenre() {
        log.trace("GenreService.getAllGenre");
        ArrayList<GenreResponse> genreResponseArrayList = new ArrayList<>();

        genreRepository.findAll().forEach(genre -> {
            genreResponseArrayList.add(
                    new GenreResponse(
                            genre.getId(),
                            genre.getName()
                    )
            );
        });

        return new MassiveGenreResponse(genreResponseArrayList.toArray(GenreResponse[]::new));
    }
}

