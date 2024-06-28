package biletka.main.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public record SalesResponse(
        @JsonProperty("time_period")
        String timePeriod,
        @JsonProperty("tickets_amount")
        Integer tickets,
        @JsonProperty("sold_amount")
        Integer sold,
        @JsonProperty("sold_percent")
        Double soldPercent,
        @JsonProperty("refunded_amount")
        Integer refunded,
        @JsonProperty("refunded_percent")
        Double refundedPercent
) {
}
