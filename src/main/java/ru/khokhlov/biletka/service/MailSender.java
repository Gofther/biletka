package ru.khokhlov.biletka.service;

import lombok.SneakyThrows;
import org.springframework.web.multipart.MultipartFile;
import ru.khokhlov.biletka.entity.Client;
import ru.khokhlov.biletka.entity.Organization;

public interface MailSender {
    void activateEmailClient(Client client);

    void activateEmailOrganization(Organization organization);

    void confirmationHallScheme(MultipartFile file, Long id, String email, Long placeId, String name);
}
