package ru.khokhlov.biletka.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record EventAdditionalInformation(
        String director,
        @JsonProperty("writer_or_artist")
        String writerOrArtist,
        String author,
        List<String> actors,
        List<String> tags
) {
}
