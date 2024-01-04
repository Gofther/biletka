package ru.khokhlov.biletka.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.khokhlov.biletka.dto.request.event_full_ei.WebWidgetRequest;
import ru.khokhlov.biletka.entity.EventWebWidget;
import ru.khokhlov.biletka.repository.WebWidgetRepository;
import ru.khokhlov.biletka.service.WebWidgetService;

@Service
@RequiredArgsConstructor
@Slf4j
public class WebWidgetServiceImpl implements WebWidgetService {
    private final WebWidgetRepository webWidgetRepository;

    @Override
    public EventWebWidget createWebWidget(WebWidgetRequest webWidgetRequest) {
        log.trace("WebWidgetServiceImpl.createWebWidget - webWidgetRequest {}", webWidgetRequest);

        EventWebWidget eventWebWidget = new EventWebWidget(
                webWidgetRequest.signature(),
                webWidgetRequest.description(),
                webWidgetRequest.ratingYandexAfisha(),
                webWidgetRequest.link()
        );

        webWidgetRepository.saveAndFlush(eventWebWidget);

        return eventWebWidget;
    }
}
