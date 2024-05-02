package biletka.main.service.Impl;

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
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
@RequiredArgsConstructor
@Slf4j
public class MailSenderImpl implements MailSender {
    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;
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
}
