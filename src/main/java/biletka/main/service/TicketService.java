package biletka.main.service;

import biletka.main.dto.request.BuyTicketRequest;
import biletka.main.dto.response.BuyTicketResponse;
import biletka.main.dto.response.MessageCreateResponse;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public interface TicketService {

    /**
     * Покупка билета
     * @param authorization токен авторизации пользователя
     * @param buyTicketRequest информация о билете
     * @return сообщение о покупке билета
     */
    BuyTicketResponse buyTicket(String authorization, BuyTicketRequest buyTicketRequest);
}
