package biletka.main.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "hall")
public class Hall {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "hall_number")
    private Integer hallNumber;

    @Column(name = "hall_name")
    private String hallName;

    @Column(name = "number_of_seats")
    private Integer numberOfSeats;

    @Column(name = "info")
    private String info;

    @Column(name = "seat_group_info")
    private String[] seatGroupInfo;

    @Lob
    @Column(name = "sheme", columnDefinition = "TEXT")
    private String scheme;

    @ManyToOne
    @JoinColumn(name = "place_id")
    private Place place;

    public Hall() {
    }

    public Hall(Integer hallNumber, String hallName, Integer numberOfSeats, String info, String[] seatGroupInfo, String scheme, Place place) {
        this.hallNumber = hallNumber;
        this.hallName = hallName;
        this.numberOfSeats = numberOfSeats;
        this.info = info;
        this.seatGroupInfo = seatGroupInfo;
        this.scheme = scheme;
        this.place = place;
    }
}
