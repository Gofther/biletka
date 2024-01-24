package ru.khokhlov.biletka.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name = "file_organization")
public class FileOrganization {
    @Id
    @Column(name = "file_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String type;

    @Lob
    @Column(name = "file_data", nullable = false, length = 1000)
    private byte[] fileData;

    public FileOrganization(String name, String type, byte[] fileData) {
        this.name = name;
        this.type = type;
        this.fileData = fileData;
    }
}
