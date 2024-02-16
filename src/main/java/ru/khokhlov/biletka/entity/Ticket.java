package ru.khokhlov.biletka.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "ticket")
public class Ticket {
    @Id
    @Column(name = "ticket_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "row_number")
    private Integer rowNumber;

    @Column(name = "seat_number")
    private Integer seatNumber;

    @Column(name = "is_reserved")
    private Boolean isReserved;

    @Column(name = "is_extinguished")
    private Boolean isExtinguished;

    @Column(name="activation_code")
    private String activationCode;

    @Column(name="price")
    private Integer price;

    @Column(name="phone")
    private String phone;

    @Column(name="email")
    private String email;

    @Column(name="fullname")
    private String fullName;

    @ManyToOne
    @JoinColumn(name = "tickets_info_id")
    private TicketsInfo info;

    public Ticket() {}

    public Ticket(Integer rowNumber, Integer seatNumber, Boolean isReserved, Boolean isExtinguished, String activationCode, Integer price, String phone, String email, String fullName) {
        this.rowNumber = rowNumber;
        this.seatNumber = seatNumber;
        this.isReserved = isReserved;
        this.isExtinguished = isExtinguished;
        this.activationCode = activationCode;
        this.price = price;
        this.phone = phone;
        this.email = email;
        this.fullName = fullName;
    }
}