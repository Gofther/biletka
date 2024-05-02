package biletka.main.entity;

import biletka.main.entity.event_item.EventAdditionalInformation;
import biletka.main.entity.event_item.EventBasicInformation;
import biletka.main.entity.event_item.EventWebWidget;
import jakarta.persistence.*;
import lombok.Data;

import java.sql.Timestamp;

@Data
@Entity
@Table(name = "event")
public class Event {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @OneToOne
    @JoinColumn(name = "widget")
    private EventWebWidget eventWebWidget;

    @OneToOne
    @JoinColumn(name = "additional")
    private EventAdditionalInformation eventAdditionalInformation;

    @OneToOne
    @JoinColumn(name = "basic")
    private EventBasicInformation eventBasicInformation;

    @Column(name = "duration")
    private String duration;

    @Column(name = "rating")
    private Double rating;

    @Column(name = "created_at")
    private Timestamp createdAt;

    @Column(name = "status")
    private Boolean status;

    public Event() {
    }

    public Event(EventWebWidget eventWebWidget, EventAdditionalInformation eventAdditionalInformation, EventBasicInformation eventBasicInformation, String duration, Double rating, Timestamp createdAt, Boolean status) {
        this.eventWebWidget = eventWebWidget;
        this.eventAdditionalInformation = eventAdditionalInformation;
        this.eventBasicInformation = eventBasicInformation;
        this.duration = duration;
        this.rating = rating;
        this.createdAt = createdAt;
        this.status = status;
    }
}
