package ru.khokhlov.biletka.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "tickets_info")
public class TicketsInfo {
    @Id
    @Column(name = "tickets_info_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "price")
    private Integer price;

    @Column(name = "sales")
    private Integer sales;

    @Column(name = "on_sales")
    private Integer onSales;

    @Column(name = "status")
    private Boolean status;

    @ManyToOne
    @JoinColumn(name = "session_id")
    private Session session;

    public TicketsInfo() {
    }

    public TicketsInfo(Integer price, Integer sales, Integer onSales, Boolean status) {
        this.price = price;
        this.sales = sales;
        this.onSales = onSales;
        this.status = status;
    }
}
