package biletka.main.repository;

import biletka.main.entity.event_item.EventAdditionalInformation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventAdditionalInformationRepository extends JpaRepository<EventAdditionalInformation, Long> {
}
