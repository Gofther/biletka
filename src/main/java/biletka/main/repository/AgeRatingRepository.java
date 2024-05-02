package biletka.main.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import biletka.main.entity.AgeRating;

public interface AgeRatingRepository extends JpaRepository<AgeRating, Long>{
    AgeRating findFirstByLimitation(int limitation);
}
