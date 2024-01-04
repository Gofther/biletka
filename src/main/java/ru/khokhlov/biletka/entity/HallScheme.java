package ru.khokhlov.biletka.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "hall_scheme")
public class HallScheme {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "hall_id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "info")
    private String info;

    @Column(name = "floor")
    private int floor;

    @Column(name = "hall_number")
    private int hallNumber;

    @Column(name = "number_seats")
    private int numberSeats;

    @Column(name = "scheme")
    private String scheme;

    @ManyToOne
    @JoinColumn(name = "place_id")
    private Place place;

    public HallScheme() {
    }

    public HallScheme(String name, String info, int floor, int hallNumber, int numberSeats, String scheme, Place place) {
        this.name = name;
        this.info = info;
        this.floor = floor;
        this.hallNumber = hallNumber;
        this.numberSeats = numberSeats;
        this.scheme = scheme;
        this.place = place;
    }
}
