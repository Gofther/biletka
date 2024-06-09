package biletka.main.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public record CityResponse(
        @JsonProperty("id")
        Long id,

        @JsonProperty("name_rus")
        String nameRus,

        @JsonProperty("name_eng")
        String nameEng
) {
}
