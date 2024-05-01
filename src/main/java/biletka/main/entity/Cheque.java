package biletka.main.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "cheque")
public class Cheque {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public Cheque() {
    }
}
