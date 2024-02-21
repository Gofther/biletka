package ru.khokhlov.biletka.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.khokhlov.biletka.entity.Ticket;
import ru.khokhlov.biletka.entity.TicketsInfo;

@Repository
public interface TicketUserRepository extends JpaRepository<Ticket, Long> {
//    @Query("SELECT t FROM Ticket t " +
//            "WHERE t.rowNumber = :row " +
//            "AND t.seatNumber = :seat")
    Ticket findFirstByRowNumberAndSeatNumber(Integer row, Integer seat);

    @Query("SELECT t FROM Ticket t " +
            "WHERE t.info.session.id = :sessionId " +
            "AND t.seatNumber = :seatNumber " +
            "AND t.rowNumber = :rowNumber")
    Ticket getFirstBySessionAndRowAndSeat(Long sessionId, Integer rowNumber, Integer seatNumber);

//    /**
//     * Запрос на поиск билета по id в бд
//     * @param id id билета
//     * @return возвращает билет
//     */
//    @Query("SELECT t FROM Ticket t " +
//            "WHERE t.id = :id")
//    TicketsInfo findTicketById(Long id);
}
