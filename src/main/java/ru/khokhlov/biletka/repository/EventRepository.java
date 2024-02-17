package ru.khokhlov.biletka.repository;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.khokhlov.biletka.entity.Event;
import ru.khokhlov.biletka.entity.EventType;

import java.util.List;

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

    @Query(value = "SELECT e FROM Event e " +
            "WHERE e.eventBasicInformation.eventType = :eventType ")
    Page<Event> findAllByTypeAndStart(EventType eventType, Pageable pageable);

    @Query("SELECT e FROM Event e ORDER BY e.id")
    Page<Event> findEventsWithLimitAndOffset(Pageable pageable);

    /**
     * Поиск в бд ивента по наличию пушкинской карты
     * @param pushkin наличие пушкинской карты
     * @return удачный/неудачный поиск ивента
     */
    @Query("SELECT e FROM Event e WHERE e.eventBasicInformation.pushkin = :pushkin")
    Page<Event> findEventsByPushkin(Boolean pushkin, Pageable pageable);

    @Query("SELECT e FROM Event e WHERE e.eventBasicInformation.ageRatingId.limitation <= :age")
    Page<Event> findEventsByAgeRatingId(int age, Pageable pageable);
}