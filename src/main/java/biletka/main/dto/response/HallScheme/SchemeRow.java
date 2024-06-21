package biletka.main.dto.response.HallScheme;

import com.fasterxml.jackson.annotation.JsonProperty;

public record SchemeRow(
        @JsonProperty("row_number")
        String rowNumber,
        @JsonProperty("scheme_seats")
        SchemeSeat[] schemeSeats
) {
        public SchemeRow withSchemeSeats(SchemeSeat[] newSchemeSeats) {
                return new SchemeRow(this.rowNumber, newSchemeSeats);
        }
}
