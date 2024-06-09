package biletka.main.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import biletka.main.entity.TypeEvent;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TypeEventRepository extends JpaRepository<TypeEvent, Long>{
    @Query("SELECT t FROM TypeEvent t " +
            "WHERE t.type = :type")
    TypeEvent findFirstByType(String type);
}
