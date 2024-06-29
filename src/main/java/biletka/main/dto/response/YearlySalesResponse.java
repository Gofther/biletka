package biletka.main.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public record YearlySalesResponse(
        @JsonProperty("year")
        int year,
        @JsonProperty("monthly_sales")
        MonthlySalesResponse[] monthlySales
) {
}
