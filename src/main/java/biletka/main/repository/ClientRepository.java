package biletka.main.repository;

import biletka.main.entity.Client;
import biletka.main.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {
    Client findFirstByUser(Users user);
}
