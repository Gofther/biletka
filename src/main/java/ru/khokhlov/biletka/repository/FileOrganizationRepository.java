package ru.khokhlov.biletka.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.khokhlov.biletka.entity.FileOrganization;

@Repository
public interface FileOrganizationRepository extends JpaRepository<FileOrganization, Long> {
}
