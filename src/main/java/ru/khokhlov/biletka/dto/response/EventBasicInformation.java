package ru.khokhlov.biletka.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record EventBasicInformation(
        String name,
        String symbolicName,
        String nameRus,
        String type,
        List<String> genre,
        @JsonProperty("age_limit")
        Integer ageLimit,
        String organizer,
        String img,
        Boolean pushkin
) {
}
