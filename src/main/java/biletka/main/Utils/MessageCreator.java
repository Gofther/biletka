package biletka.main.Utils;

import biletka.main.entity.Ticket;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class MessageCreator {
    @Value(value = "${application.server.address}")
    private String severAddress;

    public String createHallMessage(Long id) {
        return "Id зала: " + id;
    }

    public String createTicketMessage(Ticket ticket) {
        return String.format(
                "Ticket Details:\n" +
                        "ID: %d\n" +
                        "Row Number: %d\n" +
                        "Seat Number: %d\n" +
                        "Activation Code: %s\n" +
                        "Is Reserved: %b\n" +
                        "Is Extinguished: %b\n" +
                        "Is Bought: %b\n" +
                        "Is Refunded: %b\n" +
                        "Price: %.2f\n" +
                        "Email: %s\n" +
                        "Full Name: %s\n" +
                        "Phone: %s\n" +
                        "Cheque ID: %d\n" +
                        "Session ID: %d\n",
                ticket.getId(),
                ticket.getRowNumber(),
                ticket.getSeatNumber(),
                ticket.getActivationCode(),
                ticket.getIsReserved(),
                ticket.getIsExtinguished(),
                ticket.getIsBought(),
                ticket.getIsRefunded(),
                ticket.getPrice(),
                ticket.getEmail(),
                ticket.getFullName(),
                ticket.getPhone(),
                ticket.getCheque().getId(),
                ticket.getSession().getId()
        );
    }
}
