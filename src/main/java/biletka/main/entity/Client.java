package biletka.main.entity;

import biletka.main.enums.StatusUserEnum;
import jakarta.persistence.*;
import lombok.Data;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@Table(name = "client")
public class Client {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id")
    private Users user;

    @Column(name = "full_name")
    private String fullName;

    @Column(name = "birthday")
    private Timestamp birthday;

    @Column(name = "phone")
    private String phone;

    @Column(name = "created_at")
    private Timestamp createdAt;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "status")
    private StatusUserEnum status;

    @ManyToMany
    @JoinTable(
            name = "basket",
            joinColumns = @JoinColumn(name = "client_id"),
            inverseJoinColumns = @JoinColumn(name = "ticket_id")
    )
    private Set<Ticket> ticketSet = new HashSet<>();

    @ManyToMany
    @JoinTable(
            name = "favorite",
            joinColumns = @JoinColumn(name = "client_id"),
            inverseJoinColumns = @JoinColumn(name = "event_id")
    )
    private Set<Event> eventSet = new HashSet<>();

    public Client() {
    }

    public Client(Users user, String fullName, Timestamp birthday, String phone, Timestamp createdAt, StatusUserEnum status) {
        this.user = user;
        this.fullName = fullName;
        this.birthday = birthday;
        this.phone = phone;
        this.createdAt = createdAt;
        this.status = status;
    }

    public void addPlace(Ticket ticket) {
        if (ticketSet == null) ticketSet = new HashSet<>();
        if (ticket != null) this.ticketSet.add(ticket);
    }

    public void addPlace(Event event) {
        if (eventSet == null) eventSet = new HashSet<>();
        if (event != null) this.eventSet.add(event);
    }
}
