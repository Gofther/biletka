package biletka.main.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.sql.Timestamp;

@Data
@Entity
@Table(name = "place")
public class Place {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "address")
    private String address;

    @Column(name = "place_name")
    private String placeName;

    @Column(name = "created_at")
    private Timestamp createdAt;

    @ManyToOne
    @JoinColumn(name = "city_id")
    private City city;

    public Place() {
    }

    public Place(String address, String placeName, Timestamp createdAt, City city) {
        this.address = address;
        this.placeName = placeName;
        this.createdAt = createdAt;
        this.city = city;
    }
}
