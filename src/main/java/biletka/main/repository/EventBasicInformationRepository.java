package biletka.main.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import biletka.main.entity.event_item.EventBasicInformation;

public interface EventBasicInformationRepository extends JpaRepository<EventBasicInformation, Long> {
    EventBasicInformation findFirstByName(String name);
}
