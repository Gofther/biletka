package biletka.main.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import biletka.main.entity.AgeRating;
import org.springframework.data.jpa.repository.Query;

public interface AgeRatingRepository extends JpaRepository<AgeRating, Long>{
    @Query(value = "SELECT * FROM age_rating " +
            "ORDER BY ABS(age_rating.limitation - :limitation) ASC LIMIT 1;",
            nativeQuery = true)
    AgeRating findFirstByLimitation(int limitation);
}
