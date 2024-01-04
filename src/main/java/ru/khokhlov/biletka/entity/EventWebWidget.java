package ru.khokhlov.biletka.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "event_web_widget")
public class EventWebWidget {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "widget_id")
    private Long id;

    @Column(name = "signature")
    private String signature;

    @Column(name = "description")
    private String description;

    @Column(name = "rating_yandex_afisha")
    private Double ratingYandexAfisha;

    @Column(name = "link")
    private String link;

    public EventWebWidget() {
    }

    public EventWebWidget(String signature, String description, Double ratingYandexAfisha, String link) {
        this.signature = signature;
        this.description = description;
        this.ratingYandexAfisha = ratingYandexAfisha;
        this.link = link;
    }
}
