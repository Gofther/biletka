package ru.khokhlov.biletka.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.khokhlov.biletka.entity.EventDuration;

@Repository
public interface DurationRepository extends JpaRepository<EventDuration, Long> {
}
