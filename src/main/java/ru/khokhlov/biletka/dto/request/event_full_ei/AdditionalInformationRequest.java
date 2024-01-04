package ru.khokhlov.biletka.dto.request.event_full_ei;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import org.springframework.validation.annotation.Validated;

@Validated
public record AdditionalInformationRequest(
        @NotBlank(message = "Director cannot be blank")
        @Pattern(regexp = "^[a-zA-Zа-яА-Я\\s]{3,30}$", message = "Invalid director name format")
        String director,
        @NotBlank(message = "Writer or Artist cannot be blank")
        @Pattern(regexp = "^[a-zA-Zа-яА-Я\\s]{3,30}$", message = "Invalid Writer or Artist name format")
        @JsonProperty("writer_or_artist")
        String writerOrArtist,
        @NotBlank(message = "Author cannot be blank")
        @Pattern(regexp = "^[a-zA-Zа-яА-Я\\s]{3,30}$", message = "Invalid author name format")
        String author,
        @NotEmpty(message = "Actors cannot be blank")
        String[] actors,
        @NotEmpty(message = "Tags cannot be blank")
        String[] tags
) {
}
