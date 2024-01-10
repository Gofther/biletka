package ru.khokhlov.biletka.service.impl;

import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
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
    @Value(value = "${spring.mail.host}")
    private String host;
    @Value(value = "${spring.mail.username}")
    private String username;
    @Value(value = "${spring.mail.password}")
    private String password;
    @Value(value = "${spring.mail.port}")
    private int port;
    @Value(value = "${spring.mail.protocol}")
    private String protocol;

    @SneakyThrows
    @Override
    public void activateEmailClient(Client client) {
        String subject = "Подтверждение E-mail";
        String text = messageCreator.createWelcomeMessage(
                client.getFullName(),
                auth,
                client.getActivationCode());

        MimeMessage mimeMessage = mailSender.createMimeMessage();

        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
        mimeMessageHelper.setFrom(username);
        mimeMessageHelper.setTo(client.getEmail());
        mimeMessageHelper.setSubject(subject);
        mimeMessageHelper.setText(text);

        mailSender.send(mimeMessage);
    }

    @SneakyThrows
    @Override
    public void activateEmailOrganization(Organization organization) {
        String subject = "Подтверждение E-mail";
        String text = messageCreator.createWelcomeMessage(
                organization.getFullNameOrganization(),
                auth,
                organization.getActivationCode());

        MimeMessage mimeMessage = mailSender.createMimeMessage();

        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
        mimeMessageHelper.setFrom(username);
        mimeMessageHelper.setTo(organization.getEmail());
        mimeMessageHelper.setSubject(subject);
        mimeMessageHelper.setText(text);

        mailSender.send(mimeMessage);
    }

    @SneakyThrows
    private void send(String destination, String subject, String message) {
        MimeMessage mimeMessage = mailSender.createMimeMessage();

        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
        mimeMessageHelper.setFrom(username);
        mimeMessageHelper.setTo(organization);
        mimeMessageHelper.setSubject(subject);
        mimeMessageHelper.setText(message);

        mailSender.send(mimeMessage);
    }
}
