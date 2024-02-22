package ru.khokhlov.biletka.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.khokhlov.biletka.entity.Basket;
import ru.khokhlov.biletka.entity.Client;
import ru.khokhlov.biletka.entity.Session;
import ru.khokhlov.biletka.entity.TicketsInfo;

import java.util.List;

@Repository
public interface BasketRepository extends JpaRepository<Basket, Long> {

    /**
     * Запрос на поиск корзины по userId в бд
     * @param userId userId
     * @return возвращает список корзин
     */
    @Query("SELECT b FROM basket b " +
            "WHERE b.client.id = :userId")
    List<Basket> findByUserId(Long userId);

}
