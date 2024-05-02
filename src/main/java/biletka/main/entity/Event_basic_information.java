package biletka.main.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@Table(name = "event_basic_information")
public class Event_basic_information {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "symbolyc_name")
    private String symbolyc_name;

    @Column(name = "name_rus")
    private String name_rus;

    @Column(name = "organizaer")
    private String organizaer;

    @ManyToOne
    @JoinColumn(name = "age_id")
    private Age_rating ageRatingId;

    @ManyToOne
    @JoinColumn(name= "type_id")
    private Type_event typeEventId;

    @Column(name = "pushkin")
    private Boolean pushkin;

    @Column(name = "event_id_culture")
    private Long event_id_culture;

    @Column(name = "show_in_poster")
    private Boolean show_in_poster;

    @Column(name = "img")
    private String img;

    @ManyToMany
    @JoinTable(
            name = "event_genre",
            joinColumns = @JoinColumn(name = "basic_information_id"),
            inverseJoinColumns = @JoinColumn(name = "genre_id")
    )
    private Set<Genre> genres = new HashSet<>();

    public void addGenres(Genre genre) {
        if (genres == null) genres = new HashSet<>();
        if (genre != null) this.genres.add(genre);
    }

    public Event_basic_information(String name, String symbolyc_name, String name_rus, String organizaer, Age_rating ageRatingId, Type_event typeEventId, Boolean pushkin, Long event_id_culture, Boolean show_in_poster, String img, Set<Genre> genres) {
        this.name = name;
        this.symbolyc_name = symbolyc_name;
        this.name_rus = name_rus;
        this.organizaer = organizaer;
        this.ageRatingId = ageRatingId;
        this.typeEventId = typeEventId;
        this.pushkin = pushkin;
        this.event_id_culture = event_id_culture;
        this.show_in_poster = show_in_poster;
        this.img = img;
        this.genres = genres;
    }
}
