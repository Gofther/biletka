package biletka.main.repository;

import biletka.main.entity.Administrator;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

@Repository
public interface AdministratorRepository extends JpaRepository<Administrator, Long> {
    @Query("SELECT a.address FROM Administrator a")
    ArrayList<String> getAllIp();

    @Query("SELECT a FROM Administrator a " +
            "WHERE a.email = :email " +
            "AND a.address = :remoteAddr")
    Administrator findFirstByEmailAndAddress(String email, String remoteAddr);

    Administrator findFirstByEmail(String email);

    Administrator findFirstByAddress(String request);
}
