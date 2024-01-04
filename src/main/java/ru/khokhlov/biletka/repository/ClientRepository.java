package ru.khokhlov.biletka.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.khokhlov.biletka.entity.Client;

@Repository
public interface ClientRepository extends JpaRepository<Client,Long> {
    Client findByEmail(String email);

    Client findByActivationCode(String code);
}
