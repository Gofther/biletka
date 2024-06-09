package biletka.main.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "age_rating")

public class AgeRating {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column (name = "limitation")
    private int limitation;

    public AgeRating() {
    }

    public AgeRating(int limitation) {
        this.limitation = limitation;
    }
}
