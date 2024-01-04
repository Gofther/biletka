package ru.khokhlov.biletka.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@Table(name = "event")
public class Event {
    @Id
    @Column(name = "event_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "basic_info_id")
    private EventBasicInformation eventBasicInformation;

    @OneToOne
    @JoinColumn(name = "duration_id")
    private EventDuration eventDuration;

    @OneToOne
    @JoinColumn(name = "additional_information_id")
    private EventAdditionalInformation eventAdditionalInformation;

    @OneToOne
    @JoinColumn(name = "web_widget_id")
    private EventWebWidget eventWebWidget;
}
