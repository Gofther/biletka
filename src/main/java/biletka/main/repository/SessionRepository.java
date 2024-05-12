package biletka.main.repository;

import biletka.main.entity.City;
import biletka.main.entity.Event;
import biletka.main.entity.Session;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.sql.Timestamp;
import java.util.Set;

public interface SessionRepository extends JpaRepository<Session, Long> {

    @Query(value = "SELECT * FROM session " +
            "WHERE session.hall_id = :id " +
            "AND (session.start_time BETWEEN :start AND :finish " +
            "OR session.finish_time BETWEEN :start AND :finish) ", nativeQuery = true)
    Session[] findSessionsByInfo(Timestamp start, Timestamp finish, Long id);

    @Query("SELECT s.event FROM Session s " +
            "WHERE s.hall.place.city = :city " +
            "AND s.startTime >= :nowDate " +
            "ORDER BY s.id " +
            "LIMIT 10 OFFSET :offset")
    Set<Event> findAllEventByCity(City city, Integer offset, Timestamp nowDate);

    @Query("SELECT s.event FROM Session s " +
            "WHERE s.hall.place.city = :city " +
            "AND s.startTime >= :nowDate " +
            "AND s.event.createdAt >= :createDate " +
            "ORDER BY s.id " +
            "LIMIT 10 OFFSET :offset")
    Set<Event> findAllEventAdvertisementByCity(City city, Integer offset, Timestamp nowDate, Timestamp createDate);
}