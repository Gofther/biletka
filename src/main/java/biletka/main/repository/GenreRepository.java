package biletka.main.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import biletka.main.entity.Genre;

import java.util.List;

public interface GenreRepository extends JpaRepository<Genre, Long>{
    Genre findFirstByName(String name);
}
