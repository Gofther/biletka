package biletka.main.entity;

import biletka.main.enums.StatusUserEnum;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.BatchSize;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@Entity
@Table(name = "organization")
public class Organization {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private Users user;

    @Column(name = "inn")
    @BatchSize(size = 10)
    private String inn;


    @Column(name = "kbk")
    @BatchSize(size = 20)
    private String kbk;

    @Column(name = "kpp")
    @BatchSize(size = 9)
    private String kpp;

    @Column(name = "ogrn")
    @BatchSize(size = 13)
    private String ogrn;

    @Column(name = "oktmo")
    @BatchSize(size = 11)
    private String oktmo;

    @Column(name = "contact_phone")
    private String contactPhone;

    @Column(name = "email")
    private String email;

    @Column(name = "full_name_organization")
    private String fullNameOrganization;

    @Column(name = "full_name_signatory")
    private String fullNameSignatory;

    @Column(name = "legal_address")
    private String legalAddress;

    @Column(name = "name_payer")
    private String namePayer;

    @Column(name = "position_signatory")
    private String positionSignatory;

    @Column(name = "postal_address")
    @BatchSize(size = 6)
    private Integer postalAddress;

    @Column(name = "created_at")
    private Timestamp createdAt;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "status")
    private StatusUserEnum status;

    //@OneToMany
    //@JoinColumn(name = "admin_organization")
    //private Set<Event> adminEventSet;

    @ManyToMany
    @JoinTable(
            name = "event_organization",
            joinColumns = @JoinColumn(name = "organization_id"),
            inverseJoinColumns = @JoinColumn(name = "event_id")
    )
    private Set<Event> eventSet = new HashSet<>();

    @ManyToMany
    @JoinTable(
            name = "place_organization",
            joinColumns = @JoinColumn(name = "organization_id"),
            inverseJoinColumns = @JoinColumn(name = "place_id")
    )
    private Set<Place> placeSet = new HashSet<>();

    public Organization() {
    }

    public Organization(Users user, String inn, String kbk, String kpp, String ogrn, String oktmo, String contactPhone, String email, String fullNameOrganization, String fullNameSignatory, String legalAddress, String namePayer, String positionSignatory, Integer postalAddress, Timestamp createdAt, StatusUserEnum status, Set<Event> adminEventSet) {
        this.user = user;
        this.inn = inn;
        this.kbk = kbk;
        this.kpp = kpp;
        this.ogrn = ogrn;
        this.oktmo = oktmo;
        this.contactPhone = contactPhone;
        this.email = email;
        this.fullNameOrganization = fullNameOrganization;
        this.fullNameSignatory = fullNameSignatory;
        this.legalAddress = legalAddress;
        this.namePayer = namePayer;
        this.positionSignatory = positionSignatory;
        this.postalAddress = postalAddress;
        this.createdAt = createdAt;
        this.status = status;
        //this.adminEventSet = adminEventSet;
    }

    public void addEvent(Event event) {
        if (eventSet == null) eventSet = new HashSet<>();
        if (event != null) this.eventSet.add(event);
    }

    public void addPlace(Place place) {
        if (placeSet == null) placeSet = new HashSet<>();
        if (place != null) this.placeSet.add(place);
    }
}
