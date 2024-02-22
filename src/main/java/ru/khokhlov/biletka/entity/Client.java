package ru.khokhlov.biletka.entity;

import jakarta.persistence.*;
import lombok.Data;
import ru.khokhlov.biletka.enums.RoleEnum;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@Table(name = "users")
public class Client {
    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "fio")
    private String fullName;

    @Column(name = "phone")
    private String phone;

    @Column(name = "email")
    private String email;

    @Column(name = "activation_code")
    private String activationCode;

    @Column(name = "password")
    private String password;

    @Column(name = "birthday")
    private Date birthday;

    @Column(name = "favorite_genre_id")
    private int favoriteGenreId;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "role")
    private RoleEnum roleEnum;

    @ManyToMany
    @JoinTable(
            name = "basket",
            joinColumns = @JoinColumn(name = "user_id", unique = false),
            inverseJoinColumns = @JoinColumn(name = "ticket_id", unique = false)
    )
    private Set<Ticket> basket = new HashSet<>();

    @ManyToMany
    @JoinTable(
            name = "favorites",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "event_id")
    )
    private Set<Place> favorites = new HashSet<>();

    public Client() {
    }

    public Client(String fullName, String phone, String email, String activationCode, String password, Date birthday, RoleEnum role) {
        this.fullName = fullName;
        this.phone = phone;
        this.email = email;
        this.activationCode = activationCode;
        this.password = password;
        this.birthday = birthday;
        this.roleEnum = role;
    }

}
