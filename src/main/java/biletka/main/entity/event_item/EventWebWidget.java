package biletka.main.entity.event_item;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "event_web_widget")
public class EventWebWidget {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "description")
    private String description;

    @Column(name = "link")
    private String link;

    @Column(name = "signature")
    private String signature;

    public EventWebWidget() {
    }

    public EventWebWidget(String description, String link, String signature) {
        this.description = description;
        this.link = link;
        this.signature = signature;
    }
}
