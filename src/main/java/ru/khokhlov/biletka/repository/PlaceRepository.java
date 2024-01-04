package ru.khokhlov.biletka.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.khokhlov.biletka.entity.City;
import ru.khokhlov.biletka.entity.Place;

import java.util.Set;

@Repository
public interface PlaceRepository extends JpaRepository<Place, Long> {
    Place getFirstByCity(City city);
    Place getFirstByNameAndCity(String name, City city);
    Set<Place> getAllByCity(City city);
}
