package biletka.main.service.Impl;

import biletka.main.Utils.MessageCreator;
import biletka.main.dto.response.TicketResponse;
import biletka.main.entity.Cheque;
import biletka.main.entity.Event;
import biletka.main.entity.Session;
import biletka.main.entity.Ticket;
import biletka.main.repository.ChequeRepository;
import biletka.main.repository.EventRepository;
import biletka.main.service.MailSender;
import biletka.main.service.TicketService;
import com.google.zxing.WriterException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Calendar;

@Service
@RequiredArgsConstructor
@Slf4j
public class MailSenderImpl implements MailSender {
    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;
    private final MessageCreator messageCreator;
    private final ChequeRepository chequeRepository;
    @Lazy
    private final TicketService ticketService;
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

    @Async
    public void activateEmailClient(String code, String email) throws MessagingException {
        Context context = new Context();
        context.setVariable("code", code);

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, "UTF-8");

        helper.setTo(email);
        helper.setFrom(username);
        helper.setSubject("Подтверждение почты");

        String html = templateEngine.process("activationCode", context);

        helper.setText(html, true);
        mailSender.send(message);
    }

    @Async
    public void sendHall(MultipartFile file, Long hall_id) throws MessagingException {
        String subject = "Подтверждение схемы зала";
        String text = messageCreator.createHallMessage(
                hall_id);

        MimeMessage mimeMessage = mailSender.createMimeMessage();

        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
        mimeMessageHelper.setFrom(username);
        mimeMessageHelper.setTo("biletkavrn@gmail.com");
        mimeMessageHelper.setSubject(subject);
        mimeMessageHelper.addAttachment(file.getOriginalFilename(), file);
        mimeMessageHelper.setText(text);

        mailSender.send(mimeMessage);
    }
    @Async
    public void sendTicket(Ticket ticket) throws MessagingException, IOException, WriterException {
        TicketResponse ticketResponse = ticketService.getTicketResponse(ticket);
        String subject = "Покупка билета";
        Context context = new Context();
        context.setVariable("ticket", ticketResponse);

        String htmlContent = templateEngine.process("ticket", context);

        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
        mimeMessageHelper.setFrom(username);
        mimeMessageHelper.setTo(ticket.getEmail());
        mimeMessageHelper.setSubject(subject);
        mimeMessageHelper.setText(htmlContent, true);

        try {
            mailSender.send(mimeMessage);
            Cheque cheque = ticket.getCheque();
            cheque.setMail(true);
            chequeRepository.save(cheque);

            log.trace("MailSender.sendTicket / - Ticket with id {} bought and sent", ticket.getId());
        } catch (Exception e) {
            log.error("MailSender.sendTicket / - Failed to send ticket with id {}: {}", ticket.getId(), e.getMessage());
            throw e;
        }
    }
    @Async
    public void sendFile(String attachment, String email, String message) throws MessagingException, FileNotFoundException
    {
        String subject = "Информация о билетах за день";
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
        mimeMessageHelper.setFrom(username);
        mimeMessageHelper.setTo(email);
        mimeMessageHelper.setSubject(subject);
        mimeMessageHelper.setText(message);
        FileSystemResource file = new FileSystemResource(ResourceUtils.getFile(attachment));
        mimeMessageHelper.addAttachment("Информация о билетах за день", file);
        try {
            mailSender.send(mimeMessage);
            System.out.println("sent file");
            log.trace("MailSender.sendFile / - file sent");
        }catch (Exception e) {
            log.error("MailSender.sendFile / - Failed to send file");
            throw e;
        }
    }
}
