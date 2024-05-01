package biletka.main.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import biletka.main.entity.Event_basic_information;

public interface Event_basic_informationRepository extends JpaRepository<Event_basic_information, Long> {
    Event_basic_information findFirsByName(String name);
}
