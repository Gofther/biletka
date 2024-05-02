package biletka.main.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.sql.Timestamp;

@Data
@Entity
@Table(name = "session")
public class Session {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "sales")
    private Integer sales;

    @Column(name = "on_sales")
    private Integer onSales;

    @Column(name = "start_time")
    private Timestamp startTime;

    @Column(name = "number_of_views")
    private Integer numberOfViews;

    @Column(name = "price")
    private Double price;

    @Column(name = "status")
    private Boolean status;

    @ManyToOne
    @JoinColumn(name = "event_id")
    private Event event;

    @ManyToOne
    @JoinColumn(name = "hall_id")
    private Hall hall;

    @ManyToOne
    @JoinColumn(name = "movie_type_id")
    private TypeOfMovie typeOfMovie;

    public Session() {
    }

    public Session(Integer sales, Integer onSales, Timestamp startTime, Integer numberOfViews, Double price, Boolean status, Event event, Hall hall) {
        this.sales = sales;
        this.onSales = onSales;
        this.startTime = startTime;
        this.numberOfViews = numberOfViews;
        this.price = price;
        this.status = status;
        this.event = event;
        this.hall = hall;
    }
}
