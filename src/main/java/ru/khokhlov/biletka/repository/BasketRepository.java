package ru.khokhlov.biletka.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.khokhlov.biletka.entity.Basket;

@Repository
public interface BasketRepository extends JpaRepository<Basket, Long> {

}
