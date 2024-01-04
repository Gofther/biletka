package ru.khokhlov.biletka.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.khokhlov.biletka.entity.EventWebWidget;

@Repository
public interface WebWidgetRepository extends JpaRepository<EventWebWidget, Long> {
}
