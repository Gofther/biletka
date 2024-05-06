package biletka.main.dto.request;

import biletka.main.dto.request.event_item.EventAdditional;
import biletka.main.dto.request.event_item.EventBasicRequest;
import biletka.main.dto.request.event_item.EventWebWidgetRequest;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.validation.annotation.Validated;

@Validated
public record EventCreateRequest (
        @JsonProperty("event_basic")
        EventBasicRequest eventBasicRequest,

        @JsonProperty("event_additional")
        EventAdditional eventAdditional,

        @JsonProperty("event_wev_widget")
        EventWebWidgetRequest webWidget,

        @JsonProperty("duration")
        String duration
) {
}
