package biletka.main.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public record MonthlySalesResponse(
        @JsonProperty("month")
        String month,
        @JsonProperty("tickets_amount")
        Integer tickets,
        @JsonProperty("sold_amount")
        Integer sold,
        @JsonProperty("sold_percent")
        Double soldPercent
) {
}
