package biletka.main.Utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class MessageCreator {
    @Value(value = "${application.server.address}")
    private String severAddress;

    public String createHallMessage(Long id) {
        return "Id зала: " + id;
    }
}
