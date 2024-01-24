package ru.khokhlov.biletka.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@Table(name = "event_basic_information")
public class EventBasicInformation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "basic_information_id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "name_rus")
    private String nameRus;

    @Column(name = "symbolic_name")
    private String symbolicName;

    @Column(name = "organizer")
    private String organizer;

    @Column(name = "img")
    private String img;

    @Column(name = "pushkin")
    private Boolean pushkin;

    @Column(name = "event_id_culture")
    private Long eventIdCulture;

    @Column(name = "show_in_poster")
    private Boolean showInPoster;

    @OneToOne
    @JoinColumn(name = "image_id")
    private EventImage eventImage;


    @ManyToOne
    @JoinColumn(name = "event_type_id")
    private EventType eventType;

    @ManyToOne
    @JoinColumn(name = "age_rating_id")
    private AgeRating ageRatingId;

    @ManyToMany
    @JoinTable(
            name = "event_genre",
            joinColumns = @JoinColumn(name = "basic_information_id"),
            inverseJoinColumns = @JoinColumn(name = "genre_id")
    )
    private Set<Genre> genres = new HashSet<>();

    public EventBasicInformation() {
    }

    public EventBasicInformation(String name, String nameRus, String symbolicName, String organizer, String img, Boolean pushkin, Long eventIdCulture, Boolean showInPoster) {
        this.name = name;
        this.nameRus = nameRus;
        this.symbolicName = symbolicName;
        this.organizer = organizer;
        this.img = img;
        this.pushkin = pushkin;
        this.eventIdCulture = eventIdCulture;
        this.showInPoster = showInPoster;
    }

    public void addGenres(Genre genre) {
        if (genres == null) genres = new HashSet<>();
        if (genre != null) this.genres.add(genre);
    }
}
