package biletka.main.dto.request.event_item;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record EventWebWidgetRequest(
        @JsonProperty("description")
        @NotBlank(message = "Description is mandatory!")
        String description,

        @JsonProperty("link")
        @Pattern(regexp = "https://afisha.yandex.ru/", message = "Invalid link format")
        String link,
        @JsonProperty("signature")
        @NotBlank(message = "Signature is mandatory!")
        String signature
) {
}
