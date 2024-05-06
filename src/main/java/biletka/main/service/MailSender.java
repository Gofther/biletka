package biletka.main.service;

import jakarta.mail.MessagingException;
import org.springframework.stereotype.Service;

@Service
public interface MailSender {
    void activateEmailClient(String code, String email) throws MessagingException;
}
