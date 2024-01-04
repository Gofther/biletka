package ru.khokhlov.biletka.entity;

import jakarta.persistence.*;
import lombok.Data;
import ru.khokhlov.biletka.enums.RoleEnum;

import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@Table(name = "organization")
public class Organization {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "organization_id")
    private Long id;

    @Column(name = "full_name_organization")
    private String fullNameOrganization;

    @Column(name = "legal_address")
    private String legalAddress;

    @Column(name = "postal_address")
    private Integer postalAddress;

    @Column(name = "contact_phone")
    private String contactPhone;

    @Column(name = "email")
    private String email;

    @Column(name = "activation_code")
    private String activationCode;

    @Column(name = "full_name_signatory")
    private String fullNameSignatory;

    @Column(name = "position_signatory")
    private String positionSignatory;

    @Column(name = "document_contract")
    private String documentContract;

    @Column(name = "INN")
    private Integer INN;

    @Column(name = "KPP")
    private Integer KPP;

    @Column(name = "OGRN")
    private Integer OGRN;

    @Column(name = "OKTMO")
    private Integer OKTMO;

    @Column(name = "KBK")
    private Integer KBK;

    @Column(name = "name_payer")
    private String namePayer;

    @Column(name = "password")
    private String password;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "role")
    private RoleEnum roleEnum;

    @ManyToMany
    @JoinTable(
            name = "organization_place",
            joinColumns = @JoinColumn(name = "organization_id"),
            inverseJoinColumns = @JoinColumn(name = "place_id")
    )
    private Set<Place> placeSet = new HashSet<>();

    @ManyToMany
    @JoinTable(
            name = "organization_event",
            joinColumns = @JoinColumn(name = "organization_id"),
            inverseJoinColumns = @JoinColumn(name = "event_id")
    )
    private Set<Event> eventSet = new HashSet<>();

    public Organization() {
    }

    public Organization(String fullNameOrganization, String legalAddress, Integer postalAddress, String contactPhone, String email, String activationCode, String fullNameSignatory, String positionSignatory, String documentContract, Integer INN, Integer KPP, Integer OGRN, Integer OKTMO, Integer KBK, String namePayer, String password) {
        this.fullNameOrganization = fullNameOrganization;
        this.legalAddress = legalAddress;
        this.postalAddress = postalAddress;
        this.contactPhone = contactPhone;
        this.email = email;
        this.activationCode = activationCode;
        this.fullNameSignatory = fullNameSignatory;
        this.positionSignatory = positionSignatory;
        this.documentContract = documentContract;
        this.INN = INN;
        this.KPP = KPP;
        this.OGRN = OGRN;
        this.OKTMO = OKTMO;
        this.KBK = KBK;
        this.namePayer = namePayer;
        this.password = password;
    }

    public void addPlaceSet(Place place) {
        if (placeSet == null) placeSet = new HashSet<>();
        if (place != null) this.placeSet.add(place);
    }

    public void addEventSet(Event event) {
        if (eventSet == null) eventSet = new HashSet<>();
        if (event != null) this.eventSet.add(event);
    }
}
