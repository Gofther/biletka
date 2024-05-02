package biletka.main.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import biletka.main.entity.TypeEvent;
public interface TypeEventRepository extends JpaRepository<TypeEvent, Long>{
    TypeEvent findFirstByType(String type);
}
