package ru.khokhlov.biletka.service.impl;

import com.google.zxing.WriterException;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.khokhlov.biletka.entity.Client;
import ru.khokhlov.biletka.entity.Organization;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import ru.khokhlov.biletka.entity.Ticket;
import ru.khokhlov.biletka.service.MailSender;
import ru.khokhlov.biletka.utils.MessageCreator;
import ru.khokhlov.biletka.utils.QRGenerator;

import java.io.IOException;

@Service
@RequiredArgsConstructor
@Slf4j
public class MailSenderImpl implements MailSender {
    private final JavaMailSender mailSender;
    private final MessageCreator messageCreator;
    private final TemplateEngine templateEngine;
    private final QRGenerator qrGenerator;
    @Value(value = "${application.email.address}")
    private String organization;
    @Value(value = "${application.authorization.path}")
    private String auth;
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
    @Override
    public void confirmationHallScheme(MultipartFile file, Long id, String email, Long placeId, String name) {
        String subject = "Подтверждение схемы зала";
        String text = messageCreator.createHallMessage(
                id,
                email,
                placeId,
                name);

        MimeMessage mimeMessage = mailSender.createMimeMessage();

        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
        mimeMessageHelper.setFrom(username);
        mimeMessageHelper.setTo("biletkavrn@gmail.com");
        mimeMessageHelper.setSubject(subject);
        mimeMessageHelper.addAttachment(file.getOriginalFilename(), file);
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

    @Async
    public void sendTicket(Ticket ticketUser) throws MessagingException, IOException, WriterException {
        Context context = new Context();
        context.setVariable("name", ticketUser.getInfo().getSession().getEvent().getEventBasicInformation().getNameRus());
        context.setVariable("dateTime", ticketUser.getInfo().getSession().getStart().toString());
        context.setVariable("place", ticketUser.getInfo().getSession().getPlace().getAddress()+" "+ticketUser.getInfo().getSession().getPlace().getName());
        context.setVariable("hall", ticketUser.getInfo().getSession().getRoomLayout().getHallNumber());
        context.setVariable("number", ticketUser.getRowNumber());
//        context.setVariable("seat_number", ticketUser.getSeatNumber());
        context.setVariable("qr", qrGenerator.getQRCodeImage("https://google.com"));
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, "UTF-8");

        helper.setTo(ticketUser.getEmail());
        helper.setFrom(username);
        helper.setSubject("Билет \""+ticketUser.getInfo().getSession().getEvent().getEventBasicInformation().getNameRus()+"\"");

        String html = templateEngine.process("messageTicket", context);

        helper.setText(html, true);
        mailSender.send(message);
        log.info(html);
    }
}
