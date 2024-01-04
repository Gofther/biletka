package ru.khokhlov.biletka.service;

import org.springframework.stereotype.Service;
import ru.khokhlov.biletka.dto.request.TicketEditInfo;
import ru.khokhlov.biletka.dto.request.TicketInfo;
import ru.khokhlov.biletka.dto.response.SessionInfo;
import ru.khokhlov.biletka.dto.response.TicketsMassiveResponse;
import ru.khokhlov.biletka.dto.response.TicketsResponse;
import ru.khokhlov.biletka.entity.Event;
import ru.khokhlov.biletka.entity.Place;
import ru.khokhlov.biletka.entity.Session;
import ru.khokhlov.biletka.entity.TicketsInfo;

import java.util.List;

@Service
public interface TicketService {
    /**
     * Функция создания билетов
     * @param ticketInfo информация о билетах
     * @return удачное/неудачное создание билетов
     */
    TicketsResponse createTicket(TicketInfo ticketInfo);

    /**
     * Функция обновления информации билетов
     * @param ticketEditInfo изменённная информация билетов
     * @return удачное/неудачное обновление билетов
     */
    TicketsResponse editTicket(TicketEditInfo ticketEditInfo);

    /**
     * Функция вывода всех билетов
     * @return массив билетов
     */
    TicketsMassiveResponse getAllTickets();

    /**
     * Функция удаления всех билетов по сессии
     * @param session информация о сессии
     */
    void deleteAllTicketsBySession(Session session);

    /**
     * Функция удаления всех билетов по мероприятию и площадке
     * @param event информация о мероприятии
     * @param place информация о площадке
     */
    void deleteAllTicketsByEventAndPlace(Event event, Place place);

    /**
     * Функция остановки продаж билетов
     * @param session информация о сессии
     * @param status статус продажи
     */
    SessionInfo editSellSession(Session session, Boolean status);

    /**
     * Функция вывода информации билетов и сессии
     * @param session информация сессии
     * @return информация по билетам и сессии
     */
    SessionInfo getInfoTicketsAndSession(Session session);

    /**
     * Функция вывода массива билетов
     * @param place информация о площадке
     * @return массив билетов
     */
    List<TicketsInfo> getAllTicketByPlace(Place place);
}
