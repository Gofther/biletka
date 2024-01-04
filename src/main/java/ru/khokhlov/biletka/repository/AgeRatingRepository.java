package ru.khokhlov.biletka.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.khokhlov.biletka.entity.AgeRating;

public interface AgeRatingRepository extends JpaRepository<AgeRating,Long> {
    AgeRating findFirstByLimitation(int limitation);
}
