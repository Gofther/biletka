package ru.khokhlov.biletka.service;

import com.google.zxing.WriterException;
import jakarta.mail.MessagingException;
import lombok.SneakyThrows;
import org.springframework.web.multipart.MultipartFile;
import ru.khokhlov.biletka.entity.Client;
import ru.khokhlov.biletka.entity.Organization;
import ru.khokhlov.biletka.entity.Ticket;

import java.io.IOException;

public interface MailSender {
    void activateEmailClient(Client client);

    void activateEmailOrganization(Organization organization);

    void confirmationHallScheme(MultipartFile file, Long id, String email, Long placeId, String name);

    void sendTicket(Ticket ticketUser) throws MessagingException, IOException, WriterException;
}
