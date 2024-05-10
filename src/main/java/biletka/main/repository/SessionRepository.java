package biletka.main.repository;

import biletka.main.entity.Session;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.sql.Timestamp;

public interface SessionRepository extends JpaRepository<Session, Long> {
//    @Query(value = "SELECT * FROM session " +
//            "WHERE :start BETWEEN session.start_time AND session.finish_time " +
//            "AND :finish BETWEEN session.start_time AND session.finish_time " +
//            "AND session.hall_id = :id", nativeQuery = true)
//    Session findFirstByInfo(Timestamp start, Timestamp finish, Long id);

    @Query(value = "SELECT * FROM session " +
            "WHERE session.hall_id = :id " +
            "AND (session.start_time BETWEEN :start AND :finish " +
            "OR session.finish_time BETWEEN :start AND :finish) ", nativeQuery = true)
    Session[] findSessionsByInfo(Timestamp start, Timestamp finish, Long id);
}