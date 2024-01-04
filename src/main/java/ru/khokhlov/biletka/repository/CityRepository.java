package ru.khokhlov.biletka.repository;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.khokhlov.biletka.entity.City;

@Repository
public interface CityRepository extends JpaRepository<City,Long> {
    City findFirstByNameEng(String name) throws EntityNotFoundException;
}
