package ru.khokhlov.biletka.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.khokhlov.biletka.entity.Genre;
import ru.khokhlov.biletka.repository.GenreRepository;
import ru.khokhlov.biletka.service.GenreService;

import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class GenreServiceImpl implements GenreService {
    private final GenreRepository genreRepository;

    @Override
    public Set<Genre> getAllGenreByStringMassive(String[] genres) {
        log.trace("GenreServiceImpl.getAllGenreByStringMassive - genres {}", (Object) genres);

        Set<Genre> genreSet = new HashSet<>();

        for (String genre : genres) {
            if (genreRepository.findFirstByName(genre) != null)
                genreSet.add(genreRepository.findFirstByName(genre));
        }

        return genreSet;
    }

    @Override
    public void createGenre(String name) {
        log.trace("GenreServiceImpl.createGenre - name {}", name);

        if (genreRepository.findFirstByName(name) == null) {
            Genre genre = new Genre(name);
            genreRepository.saveAndFlush(genre);
        }
    }

    @Override
    public Long getIdByGenre(String name) {
        log.trace("GenreServiceImpl.getIdByGenre - name {}", name);

        return genreRepository.findFirstByName(name) == null ? -1 : genreRepository.findFirstByName(name).getId();
    }
}
