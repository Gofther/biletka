package biletka.main.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import biletka.main.entity.Actor;

public interface ActorRepository extends JpaRepository<Actor, Long>{
    Actor findFirstByName(String name);
}
