package ru.khokhlov.biletka.service;

import org.springframework.stereotype.Service;
import ru.khokhlov.biletka.dto.request.event_full_ei.WebWidgetRequest;
import ru.khokhlov.biletka.entity.EventWebWidget;

@Service
public interface WebWidgetService {
    /**
     * Функция создания информации webWidget для Event
     * @param webWidgetRequest информация виджета
     * @return удачное/неудачное создание информации webWidget
     */
    EventWebWidget createWebWidget(WebWidgetRequest webWidgetRequest);
}
