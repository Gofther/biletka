package biletka.main.repository;

import biletka.main.entity.event_item.EventWebWidget;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventWebWidgetRepository extends JpaRepository<EventWebWidget, Long> {
}
