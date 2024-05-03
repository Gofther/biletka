package biletka.main.service;

import biletka.main.dto.request.OrganizationRegistrationRequest;
import biletka.main.dto.response.MessageCreateResponse;
import biletka.main.entity.Organization;
import biletka.main.entity.Users;
import org.springframework.stereotype.Service;

@Service
public interface OrganizationService {
    /**
     * Метод добавления организации в бд
     * @param organizationRegistrationRequest данные организации
     * @param user данные о новом пользователе
     */
    void postCreateOrganization(OrganizationRegistrationRequest organizationRegistrationRequest, Users user);

    /**
     * Метод получения организации по данным
     * @param organizationRegistrationRequest данные организации
     * @return организация
     */
    Organization getOrganizationByFullNameOrganization(OrganizationRegistrationRequest organizationRegistrationRequest);
}
