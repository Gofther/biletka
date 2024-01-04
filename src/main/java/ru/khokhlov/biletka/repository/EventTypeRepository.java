package ru.khokhlov.biletka.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.khokhlov.biletka.entity.EventType;

@Repository
public interface EventTypeRepository extends JpaRepository<EventType, Long> {
    EventType findFirstByType(String type);
}
