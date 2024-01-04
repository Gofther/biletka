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

    @ManyToOne
    @JoinColumn(name = "tickets_info_id")
    private TicketsInfo info;
}