package ru.khokhlov.biletka.entity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@Entity
@Table(name = "event_image")
@AllArgsConstructor
public class EventImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "image_id")
    private Long id;

    @Lob
    @Column(name = "image_data", nullable = false, length = 1000)
    private byte[] ImageData;

    @Column(name = "image_name")
    private String ImageName;

    @Column(name = "image_type")
    private String ImageType;

}
