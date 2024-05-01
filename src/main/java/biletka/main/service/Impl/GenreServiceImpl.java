package biletka.main.service.Impl;

import biletka.main.entity.Genre;
import biletka.main.repository.GenreRepository;
import biletka.main.service.GenreService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class GenreServiceImpl implements GenreService {
    private final GenreRepository genreRepository;

    @Override
    public Genre getGenreOfId(Long id){
        Genre genre = genreRepository.getReferenceById(Long.valueOf(id));
        return genre;
    }

    @Override
    public Genre getGenreOfName(String name){
        Genre genre = genreRepository.findFirstByName(name);
        return genre;
    }
}

