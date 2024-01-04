package ru.khokhlov.biletka.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.khokhlov.biletka.entity.HallScheme;
import ru.khokhlov.biletka.entity.Place;

import java.util.List;

@Repository
public interface HallSchemeRepository extends JpaRepository<HallScheme, Long> {
    List<HallScheme> findAllByPlace(Place place);
}
