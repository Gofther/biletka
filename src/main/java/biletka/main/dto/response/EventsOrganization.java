package biletka.main.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public record EventsOrganization(
        @JsonProperty("total")
        Integer total,

        @JsonProperty("events")
        EventOrganization[] eventsOrganization
) {
}
