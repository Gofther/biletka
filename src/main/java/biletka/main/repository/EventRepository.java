package biletka.main.repository;

import biletka.main.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface EventRepository extends JpaRepository<Event, Long> {
    @Query("SELECT e FROM Event e " +
            "WHERE e.eventBasicInformation.name = :name " +
            "AND e.eventBasicInformation.typeEventId.type = :type")
    Event findFirstByName(String name, String type);

    @Query("SELECT e FROM Event e " +
            "WHERE e.id = :id " +
            "AND e.eventBasicInformation.symbolicName = :symbolicName")
    Event findFirstByIdAndSymbolicName(Long id, String symbolicName);
}
