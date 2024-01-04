package ru.khokhlov.biletka.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import ru.khokhlov.biletka.entity.Client;
import ru.khokhlov.biletka.entity.Organization;
import ru.khokhlov.biletka.service.MailSender;
import ru.khokhlov.biletka.utils.MessageCreator;

@Service
@RequiredArgsConstructor
public class MailSenderImpl implements MailSender {
    @Value(value = "${application.email.address}")
    private String organization;
    @Value(value = "${application.authorization.path}")
    private String auth;
    private final JavaMailSender mailSender;
    private final MessageCreator messageCreator;

    @Override
    public void activateEmailClient(Client client) {
        String subject = "Подтверждение E-mail";
        String text = messageCreator.createWelcomeMessage(
                client.getFullName(),
                auth,
                client.getActivationCode());

        send(client.getEmail(), subject,text);
    }

    @Override
    public void activateEmailOrganization(Organization organization) {
        String subject = "Подтверждение E-mail";
        String text = messageCreator.createWelcomeMessage(
                organization.getFullNameOrganization(),
                auth,
                organization.getActivationCode());

        send(organization.getEmail(), subject,text);
    }

    private void send(String destination, String subject, String message) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();

        mailMessage.setFrom(organization);
        mailMessage.setTo(destination);
        mailMessage.setSubject(subject);
        mailMessage.setText(message);

        mailSender.send(mailMessage);
    }
}
