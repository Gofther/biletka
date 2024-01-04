package ru.khokhlov.biletka.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.khokhlov.biletka.entity.Organization;

@Repository
public interface OrganizationRepository extends JpaRepository<Organization, Long> {
    Organization findFirstByFullNameOrganizationOrEmail(String fullNameOrganization, String email);
    Organization findByActivationCode(String code);
    Organization findByEmail(String email);
}
