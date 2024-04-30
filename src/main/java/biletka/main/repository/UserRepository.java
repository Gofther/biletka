package biletka.main.repository;

import biletka.main.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<Users, Long> {
    Users findFirstByEmail(String email);
}
