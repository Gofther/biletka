package biletka.main.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "rating")
public class Rating {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "rating")
    private Double rating;

    @OneToOne
    @JoinColumn(name = "client_id")
    private Client client;

    @OneToOne
    @JoinColumn(name = "event_id")
    private Event event;

    public Rating() {
    }

    public Rating(Double rating, Client client, Event event) {
        this.rating = rating;
        this.client = client;
        this.event = event;
    }
}
