package ru.khokhlov.biletka.service;

import org.springframework.stereotype.Service;
import ru.khokhlov.biletka.dto.request.event_full_ei.AdditionalInformationRequest;
import ru.khokhlov.biletka.entity.EventAdditionalInformation;

@Service
public interface AdditionalInformationService {
    /**
     * Функция создания дополнительной информации Event
     * @param additionalInformationRequest дополнительная информация
     * @return удачное/неудачное создание дополнительной информации
     */
    EventAdditionalInformation createAdditionalInformation(AdditionalInformationRequest additionalInformationRequest);

    /**
     * Функция удаления дополнительной информации Event
     *
     * @param eventAdditionalInformation дополнительная информация
     */
    void deleteAdditionalInformation(EventAdditionalInformation eventAdditionalInformation);
}
