package biletka.main.entity.event_item;

import biletka.main.entity.AgeRating;
import biletka.main.entity.Genre;
import biletka.main.entity.TypeEvent;
import jakarta.persistence.*;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@Table(name = "event_basic_information")
public class EventBasicInformation {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "symbolic_name")
    private String symbolicName;

    @Column(name = "name_rus")
    private String name_rus;

    @Column(name = "organizaer")
    private String organizaer;

    @ManyToOne
    @JoinColumn(name = "age_id")
    private AgeRating ageRatingId;

    @ManyToOne
    @JoinColumn(name= "type_id")
    private TypeEvent typeEventId;

    @Column(name = "pushkin")
    private Boolean pushkin;

    @Column(name = "event_id_culture")
    private Long eventIdCulture;

    @Column(name = "show_in_poster")
    private Boolean showInPoster;

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

    public EventBasicInformation() {
    }

    public EventBasicInformation(String name, String symbolicName, String name_rus, String organizaer, AgeRating ageRatingId, TypeEvent typeEventId, Boolean pushkin, Long eventIdCulture, Boolean showInPoster, String img, Set<Genre> genres) {
        this.name = name;
        this.symbolicName = symbolicName;
        this.name_rus = name_rus;
        this.organizaer = organizaer;
        this.ageRatingId = ageRatingId;
        this.typeEventId = typeEventId;
        this.pushkin = pushkin;
        this.eventIdCulture = eventIdCulture;
        this.showInPoster = showInPoster;
        this.img = img;
        this.genres = genres;
    }
}
