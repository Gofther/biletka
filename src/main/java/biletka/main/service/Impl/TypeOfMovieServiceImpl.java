package biletka.main.service.Impl;

import biletka.main.entity.TypeOfMovie;
import biletka.main.repository.TypeOfMovieRepository;
import biletka.main.service.TypeOfMovieService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class TypeOfMovieServiceImpl implements TypeOfMovieService {
    private final TypeOfMovieRepository typeOfMovieRepository;
    /**
     * Метод получения типа сеанса
     * @param name навзание типа
     * @return тип сеанса
     */
    @Override
    public TypeOfMovie getTypeByName(String name) {
        log.trace("TypeOfMovieServiceImpl.getTypeByName - name {}", name);

        TypeOfMovie typeOfMovie = typeOfMovieRepository.findFirstByTypeName(name);

        if (typeOfMovie == null) {
            typeOfMovie = new TypeOfMovie(name);
            typeOfMovieRepository.saveAndFlush(typeOfMovie);
        }

        return typeOfMovie;
    }
}
