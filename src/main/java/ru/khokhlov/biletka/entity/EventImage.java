package ru.khokhlov.biletka.entity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "event_image")
@NoArgsConstructor
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

    public EventImage(byte[] imageData, String imageName, String imageType) {
        ImageData = imageData;
        ImageName = imageName;
        ImageType = imageType;
    }
}
