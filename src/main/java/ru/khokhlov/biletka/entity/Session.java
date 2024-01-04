package ru.khokhlov.biletka.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.sql.Timestamp;

@Data
@Entity
@Table(name = "session")
public class Session {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "session_id")
    private Long id;

    @Column (name = "start_time")
    private Timestamp start;

    @ManyToOne
    @JoinColumn(name = "movie_type_id")
    private MovieViewType typeOfMovie;

    @ManyToOne
    @JoinColumn(name = "event_id")
    private Event event;

    @ManyToOne
    @JoinColumn(name = "place_id")
    private Place place;

    @ManyToOne
    @JoinColumn(name = "hall_id")
    private HallScheme roomLayout;

    public Session() {
    }

    public Session(Timestamp start) {
        this.start = start;
    }
}
