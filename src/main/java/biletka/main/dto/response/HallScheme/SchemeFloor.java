package biletka.main.dto.response.HallScheme;

import com.fasterxml.jackson.annotation.JsonProperty;

public record SchemeFloor(
        @JsonProperty("floor_number")
        Integer floorNumber,
        @JsonProperty("scheme_rows")
        SchemeRow[] schemeRows
) {
        public SchemeFloor withSchemeRows(SchemeRow[] newSchemeRows) {
                return new SchemeFloor(this.floorNumber, newSchemeRows);
        }
}
