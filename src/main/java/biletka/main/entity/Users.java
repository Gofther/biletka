package biletka.main.entity;

import biletka.main.enums.RoleEnum;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "users")
public class Users {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "role")
    private RoleEnum role;

    @Column(name = "status")
    private Boolean status;

    public Users() {
    }

    public Users(String email, String password, RoleEnum role, Boolean status) {
        this.email = email;
        this.password = password;
        this.role = role;
        this.status = status;
    }
}
