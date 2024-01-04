package ru.khokhlov.biletka.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.khokhlov.biletka.dto.request.event_full_ei.DurationRequest;
import ru.khokhlov.biletka.entity.EventDuration;
import ru.khokhlov.biletka.exception.ErrorMessage;
import ru.khokhlov.biletka.exception.InvalidDataException;
import ru.khokhlov.biletka.repository.DurationRepository;
import ru.khokhlov.biletka.service.DurationService;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class DurationServiceImpl implements DurationService {
    private final DurationRepository durationRepository;

    @Override
    public EventDuration createDuration(DurationRequest durationRequest) throws InvalidDataException {
        log.trace("DurationServiceImpl.createDuration - durationRequest {}", durationRequest);

        if (durationRequest.hours() == 0 && durationRequest.minutes() == 0){
            List<ErrorMessage> errorMessages = new ArrayList<>();
            errorMessages.add(new ErrorMessage("DurationRequest","Duration can't be 0 hours and 0 minutes"));
            throw new InvalidDataException(errorMessages);
        }

        EventDuration eventDuration = new EventDuration(
                durationRequest.hours(),
                durationRequest.minutes()
        );

        durationRepository.saveAndFlush(eventDuration);

        return eventDuration;
    }
}
