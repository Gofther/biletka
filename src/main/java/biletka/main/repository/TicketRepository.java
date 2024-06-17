package biletka.main.repository;
import biletka.main.entity.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {
    @Query("SELECT t FROM Ticket t " +
            "WHERE t.session.id = :sessionId " +
            "AND t.seatNumber = :seatNumber " +
            "AND t.rowNumber = :rowNumber")
    Ticket getFirstBySessionAndRowAndSeat(Long sessionId, Integer rowNumber, Integer seatNumber);
}