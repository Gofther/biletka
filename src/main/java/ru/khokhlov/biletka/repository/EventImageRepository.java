package ru.khokhlov.biletka.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.khokhlov.biletka.entity.EventImage;

public interface EventImageRepository extends JpaRepository<EventImage, Long> {
}
