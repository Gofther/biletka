package biletka.main.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import biletka.main.entity.Type_event;
public interface Type_eventRepository extends JpaRepository<Type_event, Long>{
    Type_event findFirstByType(String type);
}
