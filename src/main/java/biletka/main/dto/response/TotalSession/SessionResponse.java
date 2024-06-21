package biletka.main.dto.response.TotalSession;

import jakarta.persistence.Column;

import java.sql.Timestamp;

public record SessionResponse(
    Integer sales,
    Integer onSales,
    Timestamp startTime,
    Timestamp finishTime,
    Integer numberOfViews,
    Double price,
    Boolean status
) {
}
