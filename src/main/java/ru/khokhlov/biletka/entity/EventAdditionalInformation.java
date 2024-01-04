package ru.khokhlov.biletka.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@Table(name = "event_additional_information")
public class EventAdditionalInformation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "additional_id")
    private Long additionalIid;

    @Column(name = "director")
    private String director;

    @Column(name = "writer_or_artist")
    private String writerOrArtist;

    @Column(name = "author")
    private String author;

    @ManyToMany
    @JoinTable(
            name = "additional_information_actors",
            joinColumns = @JoinColumn(name = "additional_id"),
            inverseJoinColumns = @JoinColumn(name = "actor_id")
    )
    private Set<Actor> actors;

    @ManyToMany
    @JoinTable(
            name = "additional_tag",
            joinColumns = @JoinColumn(name = "additional_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private Set<Tag> tags = new HashSet<>();

    public EventAdditionalInformation() {
    }

    public EventAdditionalInformation(String director, String writerOrArtist, String author) {
        this.director = director;
        this.writerOrArtist = writerOrArtist;
        this.author = author;
    }

    public void addTags(Tag tag) {
        if (tags == null) tags = new HashSet<>();
        if (tag != null) this.tags.add(tag);
    }

    public void addActor(Actor actor){
        if (actors == null) actors = new HashSet<>();
        if (actor != null) this.actors.add(actor);
    }
}
