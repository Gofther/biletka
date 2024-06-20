package biletka.main.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "ticket")
public class Ticket {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "row_number")
    private Integer rowNumber;

    @Column(name = "seat_number")
    private Integer seatNumber;

    @Column(name = "activation_code")
    private String activationCode;

    @Column(name = "is_reserved")
    private Boolean isReserved;

    @Column(name = "is_extinguished")
    private Boolean isExtinguished;

    @Column(name = "is_bought")
    private Boolean isBought;

    @Column(name = "is_refunded")
    private Boolean isRefunded;


    @Column(name = "price")
    private Double price;

    @Column(name = "email")
    private String email;

    @Column(name = "full_name")
    private String fullName;

    @Column(name = "phone")
    private String phone;

    @OneToOne
    @JoinColumn(name = "cheque_id")
    private Cheque cheque;

    @ManyToOne
    @JoinColumn(name = "session_id")
    private Session session;

    public Ticket() {
    }

    public Ticket(Integer rowNumber, Integer seatNumber, String activationCode, Boolean isReserved, Boolean isExtinguished,Boolean isBought,Boolean isRefunded, Double price, String email, String fullName, String phone, Cheque cheque, Session session) {
        this.rowNumber = rowNumber;
        this.seatNumber = seatNumber;
        this.activationCode = activationCode;
        this.isReserved = isReserved;
        this.isExtinguished = isExtinguished;
        this.isBought = isBought;
        this.isRefunded = isRefunded;
        this.price = price;
        this.email = email;
        this.fullName = fullName;
        this.phone = phone;
        this.cheque = cheque;
        this.session = session;
    }
}
