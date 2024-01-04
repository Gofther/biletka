package ru.khokhlov.biletka.dto.request.event_full_ei;

import jakarta.validation.constraints.Min;
import org.springframework.validation.annotation.Validated;

@Validated
public record DurationRequest(
        @Min(value = 0)
        Integer hours,

        @Min(value = 0)
        Integer minutes
) {
}