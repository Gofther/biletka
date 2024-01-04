package ru.khokhlov.biletka.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.khokhlov.biletka.entity.Actor;


public interface ActorRepository extends JpaRepository<Actor, Long> {
    Actor findFirstByName(String name);
}
