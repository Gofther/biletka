package biletka.main.entity;

import biletka.main.enums.RoleEnum;
import biletka.main.enums.StatusUserEnum;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "administrator")
public class Administrator {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "code")
    private String code;

    @Column(name = "address")
    private String address;

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "role")
    private RoleEnum role;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "status")
    private StatusUserEnum status;

    public Administrator() {
    }

    public Administrator(String code, String address, String email, String password, RoleEnum role, StatusUserEnum status) {
        this.code = code;
        this.address = address;
        this.email = email;
        this.password = password;
        this.role = role;
        this.status = status;
    }
}
