package biletka.main.dto.request;

import biletka.main.dto.request.event_item.EventAdditional;
import biletka.main.dto.request.event_item.EventBasicRequest;
import biletka.main.dto.request.event_item.EventWebWidgetRequest;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import org.springframework.validation.annotation.Validated;

@Validated
public record EventCreateRequest (
        @JsonProperty("event_basic")
        @NotBlank(message = "Event basic is mandatory!")
        EventBasicRequest eventBasicRequest,

        @JsonProperty("event_additional")
        @NotBlank(message = "Event additional is mandatory!")
        EventAdditional eventAdditional,

        @JsonProperty("event_web_widget")
        @NotBlank(message = "Event web widget is mandatory!")
        EventWebWidgetRequest webWidget,

        @JsonProperty("duration")
        @NotBlank(message = "Duration is mandatory!")
        String duration
) {
}
