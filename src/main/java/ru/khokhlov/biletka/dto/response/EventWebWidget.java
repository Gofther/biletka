package ru.khokhlov.biletka.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public record EventWebWidget(
        String signature,
        String description,
        @JsonProperty("rating_yandex_afisha")
        Double ratingYandexAfisha,
        String link
) {
}
