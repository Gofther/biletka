package ru.khokhlov.biletka.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.khokhlov.biletka.entity.Event;
import ru.khokhlov.biletka.entity.Place;
import ru.khokhlov.biletka.entity.Session;
import ru.khokhlov.biletka.entity.TicketsInfo;

import java.util.List;

@Repository
public interface TicketRepository extends JpaRepository<TicketsInfo, Long> {
    /**
     * Запрос на поиск билета по session в бд
     * @param sessionId информация о сессии
     * @return возвращает билет
     */
    @Query("SELECT t FROM TicketsInfo t " +
            "WHERE t.session.id = :sessionId")
    List<TicketsInfo> findAllBySession(Long sessionId);

    /**
     * Запрос на обновление ифнормации о билетах
     * @param id id билетов
     * @param price цена билетов
     * @param sales количество проданных билетов
     * @param onSales количество продаваемых билетов
     */
    @Modifying
    @Transactional
    @Query("UPDATE TicketsInfo t " +
            "SET t.price = :price, " +
            "t.sales = :sales, " +
            "t.onSales = :onSales " +
            "WHERE t.id = :id")
    void editTicket(Long id, Integer price, Integer sales, Integer onSales);

    /**
     * Запрос на вывод всех билетов по мероприятию
     * @param event мероприятие
     * @return возвращает билеты
     */
    @Query("SELECT t FROM TicketsInfo t " +
            "WHERE t.session.event = :event")
    List<TicketsInfo> getAllTicketsByEvent(Event event);

    /**
     * Запрос на вывод всех билетов по площадке
     * @param place информация о площадке
     * @return возвращает билеты
     */
    @Query("SELECT t FROM TicketsInfo t " +
            "WHERE t.session.place = :place")
    List<TicketsInfo> getAllTicketByOrganization(Place place);

    /**
     * Запрос на вывод всех билетов по мероприятию и площадке
     * @param place информация о площадке
     * @param event информация о мероприятии
     * @return возвращает билеты
     */
    @Query("SELECT t FROM TicketsInfo t " +
            "WHERE t.session.place = :place " +
            "AND t.session.event = :event")
    List<TicketsInfo> getAllTicketByOrganizationAndEvent(Place place, Event event);

    /**
     * Запрос на удаление билетов по сессии
     * @param sessionId информация сессии
     */
    @Modifying
    @Query("DELETE FROM TicketsInfo t " +
            "WHERE t.session.id = :sessionId")
    void deleteAllBySessionId(Long sessionId);

    /**
     * Запрос на удаление билетов по площадке и мероприятию
     * @param eventId информация о мероприятии
     * @param placeId информация о площадке
     */
    @Modifying
    @Query("DELETE FROM Session s " +
            "WHERE s.event.id = :eventId AND s.place.id = :placeId")
    void deleteAllBySession_EventAndSession_Place(Long eventId, Long placeId);

    /**
     * Запрос на поиск билета по session в бд
     * @param session информация о сессии
     * @return возвращает билет
     */
    @Query("SELECT t FROM TicketsInfo t " +
            "WHERE t.session = :session")
    TicketsInfo findFirstBySession(Session session);

    /**
     * Запрос на поиск билета по session в бд
     * @param id id сессии
     * @return возвращает билет
     */
    @Query("SELECT t FROM TicketsInfo t " +
            "WHERE t.session.id = :id")
    TicketsInfo findFirstBySessionId(Long id);

    @Modifying
    @Query("UPDATE TicketsInfo t " +
            "SET t.onSales = t.onSales - 1, " +
            "t.sales = t.sales + 1 " +
            "WHERE t.id = :id")
    void buyOneTicket(Long id);

    @Query("SELECT t FROM TicketsInfo t " +
            "WHERE t.session.event = :event " +
            "AND t.session.place = :place")
    List<TicketsInfo> getAllTicketByEventAndPlace(Place place, Event event);


    @Query("SELECT t FROM TicketsInfo t " +
            "WHERE t.session.id = :id")
    TicketsInfo getTicketsBySession(Long id);
}