package biletka.main.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.criteria.CriteriaBuilder;

public record MonthlySessionsResponse(
        @JsonProperty("time_period")
        String timePeriod,
        @JsonProperty("all_sessions_amount")
        Integer allSessionsAmount,
        @JsonProperty("places")
        PlacesSessionsResponse[] placesSessionsResponses
) {
}
