package ru.khokhlov.biletka.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.khokhlov.biletka.entity.Session;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface SessionRepository extends JpaRepository<Session, Long> {
    //TODO переписать правильно запрос
    @Query(value = "SELECT s FROM Session s " +
            "WHERE s.event.id = :eventId " +
            "AND s.place.city.cityId =:cityId")
    List<Session> findAllByEventIdAndPlaceCity_CityId(Long eventId, Long cityId);

    @Query(value = "SELECT s FROM Session s " +
            "WHERE s.place.city.cityId =:cityId")
    List<Session> findAllByPlaceCity_CityId(Long cityId);

    @Query(value = "SELECT s FROM Session s " +
            "WHERE s.place.city.cityId = :cityId " +
            "AND DATE(s.start) = :date")
    List<Session> findAllByStartAndPlaceCity_CityId(LocalDate date, Long cityId);

    @Query(value = "SELECT s FROM Session s " +
            "WHERE s.place.id = :placeId")
    List<Session> findAllByPlaceId(Long placeId);

    @Query(value = "SELECT s FROM Session s " +
            "WHERE s.event.id = :eventId " +
            "AND s.place.city.cityId =:cityId AND DATE(s.start) = :date"
            )
    List<Session> findAllByEventIdDateAndCityId(Long eventId, Long cityId, LocalDate date);

    @Modifying
    @Query(value = "DELETE FROM Session s " +
            "WHERE s.event.id = :eventId " +
            "AND s.place.id = :placeId")
    void deleteAllByEventAndPlace(Long eventId, Long placeId);

    @Modifying
    @Query(value = "DELETE FROM Session s " +
            "WHERE s.id = :id")
    void deleteSession(Long id);
}
