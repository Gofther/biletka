package biletka.main.entity;

import biletka.main.entity.event_item.EventAdditionalInformation;
import biletka.main.entity.event_item.EventBasicInformation;
import biletka.main.entity.event_item.EventWebWidget;
import biletka.main.enums.StatusEventEnum;
import jakarta.persistence.*;
import lombok.Data;

import java.sql.Timestamp;

@Data
@Entity
@Table(name = "event")
public class Event {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "widget")
    private EventWebWidget eventWebWidget;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "additional")
    private EventAdditionalInformation eventAdditionalInformation;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "basic")
    private EventBasicInformation eventBasicInformation;

    @Column(name = "duration")
    private String duration;

    @Column(name = "rating")
    private Double rating;

    @Column(name = "created_at")
    private Timestamp createdAt;

    @Column(name = "status")
    private StatusEventEnum status;

    public Event() {
    }

    public Event(EventWebWidget eventWebWidget, EventAdditionalInformation eventAdditionalInformation, EventBasicInformation eventBasicInformation, String duration, Double rating, Timestamp createdAt, StatusEventEnum status) {
        this.eventWebWidget = eventWebWidget;
        this.eventAdditionalInformation = eventAdditionalInformation;
        this.eventBasicInformation = eventBasicInformation;
        this.duration = duration;
        this.rating = rating;
        this.createdAt = createdAt;
        this.status = status;
    }
}
