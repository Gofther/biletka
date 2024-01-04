package ru.khokhlov.biletka.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.khokhlov.biletka.entity.MovieViewType;

@Repository
public interface MovieViewTypeRepository extends JpaRepository<MovieViewType, Long> {
    MovieViewType getFirstByMvtName(String typeOfMovie);
}
