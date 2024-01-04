package ru.khokhlov.biletka.service;

import ru.khokhlov.biletka.entity.Client;
import ru.khokhlov.biletka.entity.Organization;

public interface MailSender {
    void activateEmailClient(Client client);

    void activateEmailOrganization(Organization organization);
}
