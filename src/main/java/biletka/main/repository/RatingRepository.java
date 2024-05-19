package biletka.main.repository;

import biletka.main.entity.Client;
import biletka.main.entity.Event;
import biletka.main.entity.Rating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface RatingRepository extends JpaRepository<Rating, Long> {
    Rating findFirstByClientAndEvent(Client client, Event event);

    @Query("SELECT COUNT(r) FROM Rating r " +
            "WHERE r.event = :event")
    Double findTotalRatingByEvent(Event event);
}
