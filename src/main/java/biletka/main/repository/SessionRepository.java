package biletka.main.repository;

import biletka.main.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.sql.Timestamp;
import java.util.ArrayList;
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

    @Query("SELECT s FROM Session s " +
            "WHERE s.event = :event " +
            "AND s.hall.place.city = :city " +
            "AND s.startTime BETWEEN :startDay AND :finishDay " +
            "ORDER BY s.hall.place, s.startTime ")
    ArrayList<Session> findAllSessionByEventAndCity(Event event, City city, Timestamp startDay, Timestamp finishDay);

    @Query("SELECT s.event FROM Session s " +
            "WHERE s.hall.place.city = :city " +
            "AND s.event.eventBasicInformation.ageRatingId.limitation <= :age " +
            "AND s.startTime >= :nowDate " +
            "ORDER BY s.id " +
            "LIMIT 10 OFFSET :offset")
    Set<Event> findAllEventByCityAndAge (City city, int age, Integer offset, Timestamp nowDate);

    @Query("SELECT s.event FROM Session s " +
            "WHERE s.hall.place.city = :city " +
            "AND s.event.eventBasicInformation.typeEventId.type = :type " +
            "AND s.startTime >= :nowDate " +
            "ORDER BY s.id " +
            "LIMIT 10 OFFSET :offset")
    Set<Event> findAllEventByCityAndType (City city, String type, Integer offset, Timestamp nowDate);

    @Query("SELECT s.event FROM Session s " +
            "WHERE s.hall.place.city = :city " +
            "AND EXISTS(SELECT g FROM s.event.eventBasicInformation.genres g " +
            "WHERE g = :genre) " +
            "AND s.startTime >= :nowDate " +
            "ORDER BY s.id " +
            "LIMIT 10 OFFSET :offset")
    Set<Event> findAllEventByCityAndGenre (City city, Genre genre, Integer offset, Timestamp nowDate);

    @Query("SELECT s FROM Session s " +
    "WHERE s.hall.place = :place " +
    "AND s.event = :event ")
    Set<Session> findAllSessionByPlaceAndEvent(Place place, Event event);

    @Query("SELECT s.event FROM Session s " +
    "WHERE s.hall.place = :place ")
    Set<Event> findEventsByPlace(Place place);
}

