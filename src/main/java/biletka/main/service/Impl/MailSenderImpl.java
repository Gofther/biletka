package biletka.main.service.Impl;

import biletka.main.Utils.MessageCreator;
import biletka.main.entity.Cheque;
import biletka.main.entity.Ticket;
import biletka.main.repository.ChequeRepository;
import biletka.main.service.MailSender;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
@RequiredArgsConstructor
@Slf4j
public class MailSenderImpl implements MailSender {
    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;
    private final MessageCreator messageCreator;
    private final ChequeRepository chequeRepository;
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
    public void sendTicket(Ticket ticket) throws MessagingException {
        String subject = "Покупка билета";

        String text = messageCreator.createTicketMessage(ticket);

        MimeMessage mimeMessage = mailSender.createMimeMessage();

        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
        mimeMessageHelper.setFrom(username);
        mimeMessageHelper.setTo(ticket.getEmail());
        mimeMessageHelper.setSubject(subject);
        mimeMessageHelper.setText(text);

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
}
