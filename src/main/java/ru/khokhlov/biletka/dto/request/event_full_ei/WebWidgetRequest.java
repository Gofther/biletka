package ru.khokhlov.biletka.dto.request.event_full_ei;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import org.springframework.validation.annotation.Validated;

@Validated
public record WebWidgetRequest(
        @NotBlank(message = "Signature must not be blank")
        String signature,

        @NotBlank(message = "Description must not be blank")
        String description,

        @Min(value = 0)
        @Max(value = 10)
        @JsonProperty("rating_yandex_afisha")
        Double ratingYandexAfisha,

        @NotBlank(message = "Link must not be blank")
        @Pattern(regexp = "https?://.+",
                message = "Link must start with 'http://' or 'https://'")
        String link
) {
}
