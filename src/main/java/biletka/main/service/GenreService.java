package biletka.main.service;

import biletka.main.entity.Genre;
import org.springframework.stereotype.Service;

@Service
public interface GenreService {
    Genre getGenreOfId(Long id);

    Genre getGenreOfName(String name);
}
