package biletka.main.service;

import biletka.main.dto.request.BuyTicketRequest;
import biletka.main.dto.response.BuyTicketResponse;
import biletka.main.dto.response.MessageCreateResponse;
import biletka.main.dto.response.TicketResponse;
import biletka.main.entity.Ticket;
import com.google.zxing.WriterException;
import jakarta.mail.MessagingException;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Optional;

@Service
public interface TicketService {

    /**
     * Покупка билета
     * @param authorization токен авторизации пользователя
     * @param buyTicketRequest информация о билете
     * @return сообщение о покупке билета
     */
    BuyTicketResponse buyTicket(String authorization, BuyTicketRequest buyTicketRequest) throws MessagingException;

    /**
     * Получение данных о билете
     * @param ticket билет
     * @return данные о билете для отправки на почту
     */
    TicketResponse getTicketResponse(Ticket ticket) throws IOException, WriterException;
}
