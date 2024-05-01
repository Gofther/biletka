package biletka.main.entity.event_item;

import jakarta.persistence.*;
import lombok.Data;

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

    public EventAdditionalInformation() {
    }
}
