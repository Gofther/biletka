package biletka.main.service;

import biletka.main.dto.request.event_item.EventWebWidgetRequest;
import biletka.main.entity.event_item.EventWebWidget;
import org.springframework.stereotype.Service;

@Service
public interface EventWebWidgetService {
    /**
     * Метод создания виджета мероприятия
     * @param eventWebWidget информация для виджета
     * @return виджет
     */
    EventWebWidget createEventWebWidget(EventWebWidgetRequest eventWebWidget);
}
