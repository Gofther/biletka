package biletka.main.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import biletka.main.entity.Age_rating;

public interface Age_ratingRepository extends JpaRepository<Age_rating, Long>{
    Age_rating findFirstByLimitation(int limitation);
}
