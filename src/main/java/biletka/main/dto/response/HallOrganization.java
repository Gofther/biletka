package biletka.main.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public record HallOrganization(
        @JsonProperty("hall_id")
        Long hallId,

        @JsonProperty("hall_number")
        Integer hallNumber,

        @JsonProperty("hall_name")
        String hallName,

        @JsonProperty("seats")
        Integer seats,

        @JsonProperty("info")
        String info,

        @JsonProperty("status")
        boolean status
) {
}
