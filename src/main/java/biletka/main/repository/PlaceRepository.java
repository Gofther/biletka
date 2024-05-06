package biletka.main.repository;

import biletka.main.entity.Place;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlaceRepository extends JpaRepository<Place, Long> {
    Place findFirstByAddressAndPlaceName(String address, String place_name);
}
