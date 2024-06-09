package biletka.main.service;

import jakarta.mail.MessagingException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public interface MailSender {
    void activateEmailClient(String code, String email) throws MessagingException;

    void  sendHall(MultipartFile file, Long hall_id)  throws MessagingException;
}
