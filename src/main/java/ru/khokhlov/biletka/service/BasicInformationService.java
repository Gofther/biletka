package ru.khokhlov.biletka.service;

import org.springframework.stereotype.Service;
import ru.khokhlov.biletka.dto.request.event_full_ei.BasicInformationRequest;
import ru.khokhlov.biletka.entity.EventBasicInformation;
import ru.khokhlov.biletka.entity.EventImage;

@Service
public interface BasicInformationService {
    /**
     * Функция создания основной информации для Event
     *
     * @param basicInformationRequest основная информация
     * @return удачное/неудачное создание создания основной информации
     */
    EventBasicInformation createBasicInformation(BasicInformationRequest basicInformationRequest);

    /**
     * Функция удаления основной информации для Event
     *
     * @param eventBasicInformation основная информация
     */
    void deleteBasicInformation(EventBasicInformation eventBasicInformation);

    void addImage(EventBasicInformation eventBasicInformation, EventImage eventImage);
}
