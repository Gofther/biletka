package biletka.main.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "type_of_movie")
public class TypeOfMovie {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "type_name")
    private String typeName;

    public TypeOfMovie() {
    }

    public TypeOfMovie(String typeName) {
        this.typeName = typeName;
    }
}
