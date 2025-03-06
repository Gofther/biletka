package biletka.main.service;

import biletka.main.entity.Ticket;
import com.google.zxing.WriterException;
import jakarta.mail.MessagingException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;

@Service
public interface MailSender {
    void activateEmailClient(String code, String email) throws MessagingException;

    void  sendHall(MultipartFile file, Long hall_id)  throws MessagingException;

    void sendTicket(Ticket ticket) throws MessagingException, IOException, WriterException;

    void sendFile(String attachment, String email, String message) throws MessagingException, FileNotFoundException;


}
