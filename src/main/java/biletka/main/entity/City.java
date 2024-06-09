package biletka.main.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "city")
public class City {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "city_name")
    private String cityName;

    @Column(name = "city_name_eng")
    private String cityNameEng;

    public City() {
    }

    public City(String cityName, String cityNameEng) {
        this.cityName = cityName;
        this.cityNameEng = cityNameEng;
    }
}
