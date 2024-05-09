package biletka.main.service;

import biletka.main.entity.TypeOfMovie;
import org.springframework.stereotype.Service;

@Service
public interface TypeOfMovieService {
    /**
     * Метод получения типа сеанса
     * @param name навзание типа
     * @return тип сеанса
     */
    TypeOfMovie getTypeByName(String name);
}
