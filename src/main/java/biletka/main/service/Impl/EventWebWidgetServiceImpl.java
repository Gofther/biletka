package biletka.main.service.Impl;

import biletka.main.dto.request.event_item.EventWebWidgetRequest;
import biletka.main.entity.event_item.EventWebWidget;
import biletka.main.repository.EventWebWidgetRepository;
import biletka.main.service.EventWebWidgetService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class EventWebWidgetServiceImpl implements EventWebWidgetService {
    private final EventWebWidgetRepository eventWebWidgetRepository;

    /**
     * Метод создания виджета мероприятия
     * @param eventWebWidget информация для виджета
     * @return виджет
     */
    @Override
    public EventWebWidget createEventWebWidget(EventWebWidgetRequest eventWebWidget) {
        log.trace("EventWebWidgetServiceImpl.createEventWebWidget - eventWebWidgetRequest {}", eventWebWidget);

        EventWebWidget eventWebWidgetNew = new EventWebWidget(
                eventWebWidget.description(),
                eventWebWidget.link(),
                eventWebWidget.signature()
        );

        eventWebWidgetRepository.saveAndFlush(eventWebWidgetNew);

        return eventWebWidgetNew;
    }
}
