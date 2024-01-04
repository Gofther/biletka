package ru.khokhlov.biletka.repository;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.khokhlov.biletka.entity.Event;
import ru.khokhlov.biletka.entity.EventType;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
    /**
     * Поиск в бд ивента по типу ивента и символичному названию ивента
     * @param eventType тип ивента
     * @param symbolicName символичное название ивента
     * @return удачный/неудачный поиск ивента
     */
    @Query("SELECT e FROM Event e " +
            "WHERE e.eventBasicInformation.eventType = :eventType " +
            "AND e.eventBasicInformation.symbolicName = :symbolicName")
    Event findFirstByEventTypeAndSymbolicName(EventType eventType, String symbolicName) throws EntityNotFoundException;
}
