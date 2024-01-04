package ru.khokhlov.biletka.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "city")
public class City {
    @Id
    @Column(name = "city_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cityId;

    @Column(name = "city_name")
    private String nameRus;

    @Column(name = "city_name_eng")
    private String nameEng;
}