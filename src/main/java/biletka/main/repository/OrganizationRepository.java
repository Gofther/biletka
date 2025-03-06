package biletka.main.repository;

import biletka.main.entity.Organization;
import biletka.main.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface OrganizationRepository extends JpaRepository<Organization, Long> {
    Organization findFirstByFullNameOrganization(String fullNameOrganization);

    /**
     * SQL запрос на поиск организации по возможным данным
     * @param inn ИНН орагнизации
     * @param kbk КБК  орагнизации
     * @param kpp КПП  орагнизации
     * @param ogrn ОГРН орагнизации
     * @param oktmo ОКТМО орагнизации
     * @param email почта орагнизации
     * @param fullNameOrganization название орагнизации
     * @return организация
     */
    @Query("SELECT o FROM Organization o " +
            "WHERE o.inn = :inn " +
            "OR o.kbk = :kbk " +
            "OR o.kpp = :kpp " +
            "OR o.ogrn = :ogrn " +
            "OR o.oktmo = :oktmo " +
            "OR o.email = :email " +
            "OR o.fullNameOrganization = :fullNameOrganization")
    Organization findFirstByFullInfo(String inn, String kbk, String kpp, String ogrn, String oktmo, String email, String fullNameOrganization);

    Organization findFirstByUser(Users user);

}
