package biletka.main.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "cheque")
public class Cheque {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "url")
    private String url;

    @Enumerated
    @Column(name = "status")
    private Status status;

    @Column(name = "mail_sent")
    boolean mail;

    public Cheque() {
    }

    public enum Status {
        BUY,REF,CANCEL
    }

    public Cheque(String url, Status status, boolean mail){
        this.url = url;
        this.status = status;
        this.mail = mail;
    }
}
