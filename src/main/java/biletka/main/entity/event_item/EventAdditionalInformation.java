package biletka.main.entity.event_item;

import biletka.main.entity.Actor;
import biletka.main.entity.Genre;
import biletka.main.entity.Tag;
import jakarta.persistence.*;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@Table(name = "event_additional_information")
public class EventAdditionalInformation {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "author")
    private String author;

    @Column(name = "director")
    private String director;

    @Column(name = "writer_or_artist")
    private String writerOrArtist;

    @ManyToMany
    @JoinTable(
            name = "additional_actor",
            joinColumns = @JoinColumn(name = "additional_id"),
            inverseJoinColumns = @JoinColumn(name = "actor_id")
    )
    private Set<Actor> actorSet = new HashSet<>();

    @ManyToMany
    @JoinTable(
            name = "additional_tag",
            joinColumns = @JoinColumn(name = "additional_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private Set<Tag> tagSet = new HashSet<>();

    public void addGenres(Actor actor) {
        if (actorSet == null) actorSet = new HashSet<>();
        if (actor != null) this.actorSet.add(actor);
    }

    public void addGenres(Tag tag) {
        if (tagSet == null) tagSet = new HashSet<>();
        if (tag != null) this.tagSet.add(tag);
    }

    public EventAdditionalInformation() {
    }

    public EventAdditionalInformation(String author, String director, String writerOrArtist, Set<Actor> actorSet, Set<Tag> tagSet) {
        this.author = author;
        this.director = director;
        this.writerOrArtist = writerOrArtist;
        this.actorSet = actorSet;
        this.tagSet = tagSet;
    }
}
