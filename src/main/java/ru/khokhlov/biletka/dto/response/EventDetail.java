package ru.khokhlov.biletka.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public record EventDetail(
        @JsonProperty("basic_information")
        EventBasicInformation eventBasicInformation,
        EventDuration duration,
        @JsonProperty("addition_information")
        EventAdditionalInformation eventAdditionalInformation,
        @JsonProperty("web_widget")
        EventWebWidget eventWebWidget
) {
}
