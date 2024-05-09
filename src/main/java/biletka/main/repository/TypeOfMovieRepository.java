package biletka.main.repository;

import biletka.main.entity.TypeOfMovie;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TypeOfMovieRepository extends JpaRepository<TypeOfMovie, Long> {
    TypeOfMovie findFirstByTypeName(String typeName);
}
