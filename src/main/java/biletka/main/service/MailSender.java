package biletka.main.service;

import jakarta.mail.MessagingException;

public interface MailSender {
    void activateEmailClient(String code, String email) throws MessagingException;
}
