package ru.khokhlov.biletka.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "event_duration")
public class EventDuration {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "duration_id")
    private Long id;

    @Column(name = "hours")
    private Integer hours;

    @Column(name = "minutes")
    private Integer minutes;

    public EventDuration() {
    }

    public EventDuration(Integer hours, Integer minutes) {
        this.hours = hours;
        this.minutes = minutes;
    }
}
