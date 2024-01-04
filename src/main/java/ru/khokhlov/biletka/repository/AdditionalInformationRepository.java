package ru.khokhlov.biletka.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.khokhlov.biletka.entity.EventAdditionalInformation;

@Repository
public interface AdditionalInformationRepository extends JpaRepository<EventAdditionalInformation, Long> {
}
