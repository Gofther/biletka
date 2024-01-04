package ru.khokhlov.biletka.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@Table(name = "type_of_movie")
public class MovieViewType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "movie_type_id")
    private Long id;

    @Column(name = "type_name")
    private String mvtName;

    public MovieViewType(String mvtName) {
        this.mvtName = mvtName;
    }
}

