package ru.khokhlov.biletka.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.khokhlov.biletka.entity.EventBasicInformation;

@Repository
public interface BasicInformationRepository extends JpaRepository<EventBasicInformation, Long> {
    /**
     * Поиск в бд основной информации по названию ивента
     * @param name название ивента
     * @return удачный/неудачный поиск основной информации
     */
    EventBasicInformation findFirstByName(String name);
}
