package biletka.main.repository;

import biletka.main.entity.City;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CityRepository extends JpaRepository<City, Long> {
    City findFirstByCityNameEng(String cityNameEng);
}
