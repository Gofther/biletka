package biletka.main.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "age_rating")

public class Age_rating {
    @Id
    @Column(name = "age_rating_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column (name = "limitation")
    private int limitation;
}
