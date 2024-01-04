package ru.khokhlov.biletka.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;
import ru.khokhlov.biletka.dto.request.event_full_ei.AdditionalInformationRequest;
import ru.khokhlov.biletka.dto.request.event_full_ei.BasicInformationRequest;
import ru.khokhlov.biletka.dto.request.event_full_ei.DurationRequest;
import ru.khokhlov.biletka.dto.request.event_full_ei.WebWidgetRequest;

@Validated
public record EventInfo(
        @Valid
        @JsonProperty("basic_information")
        BasicInformationRequest basicInformation,

        @Valid
        @JsonProperty("duration")
        DurationRequest duration,

        @Valid
        @JsonProperty("additional_information")
        AdditionalInformationRequest additionalInformationRequest,

        @Valid
        @JsonProperty("web_widget")
        WebWidgetRequest webWidget
) {
}
