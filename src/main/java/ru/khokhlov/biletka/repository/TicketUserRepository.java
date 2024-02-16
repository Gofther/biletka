package ru.khokhlov.biletka.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.khokhlov.biletka.entity.Ticket;

@Repository
public interface TicketUserRepository extends JpaRepository<Ticket, Long> {

}
