package ru.khokhlov.biletka.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity(name = "basket")
public class Basket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "basket_id")
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id")
    private Client client;

    @OneToOne
    @JoinColumn(name = "ticket_id")
    private Ticket ticket;
}
