package biletka.main.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public record MassiveClientTicketResponse(
        @JsonProperty("tickets")
        ClientTicketResponse[] tickets
) {
}
