package ru.khokhlov.biletka.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.khokhlov.biletka.entity.Genre;

@Repository
public interface GenreRepository extends JpaRepository<Genre, Long> {
    Genre findFirstByName(String name);
}
