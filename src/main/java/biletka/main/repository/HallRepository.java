package biletka.main.repository;

import biletka.main.entity.Hall;
import biletka.main.entity.Place;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HallRepository extends JpaRepository<Hall, Long> {
    Hall findFirstByPlaceAndHallNumber(Place place, Integer hallNumber);
}
